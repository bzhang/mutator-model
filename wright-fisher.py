"""
wright-fisher.py: Wright-Fisher implementation of mutator evolution model described in Gerrish et al. (2007) Complete genetic linkage can subvert natural selection. PNAS 104: 6266-71.

Contains the Individual, Population, and Evolution classes.

Usage:

    For details on the parameters (including their default values) run the command:

    >>> python wright-fisher.py -h

    For example, to run a simulation with population size of 100 for 200 generations and other parameters set to their default values run the command:

    >>> python wright-fisher.py --pop_size 100 --gen 200
"""


import numpy as np
import copy
import argparse


__author__  = 'Ricardo Azevedo, Bingjun Zhang, Ata Kalirad'
__version__ = "0.1.2"


class Individual(object):
    """
    Define an Individual object, characterized by numbers of beneficial, deleterious, mutator and antimutator mutations, fitness and mutation rate.

    Attributes:

        base_mut_rate -- base genomic mutation rate
        f_deleterious -- fraction of deleterious mutations
        f_beneficial  -- fraction of beneficial mutations
        f_mutator     -- fraction of mutator mutations
        f_antimutator -- fraction of antimutator mutations
        f_lethal      -- fraction of lethal mutations out of the deleterious mutations
        M_deleterious -- average fitness effect of a deleterious mutation
        M_beneficial  -- average fitness effect of a beneficial mutation
        M_mutator     -- average fitness effect of a mutator mutation
        M_antimutator -- average fitness effect of a antimutator mutation
        n_deleterious -- number of deleterious mutations
        n_beneficial  -- number of beneficial mutations
        n_mutator     -- number of mutator mutations
        n_antimutator -- number of antimutator mutations
        n_lethal      -- number of lethal mutations
        fitness       -- fitness
        mut_rate      -- relative genomic mutation rate
    """

    def __init__(self, base_mut_rate, f_deleterious, f_beneficial, f_mutator, f_antimutator, f_lethal, M_deleterious, M_beneficial, M_mutator, M_antimutator):
        """
        Create an Individual object with certain population genetic parameters.

        Parameters:

            base_mut_rate -- base genomic mutation rate
            f_deleterious -- fraction of deleterious mutations
            f_beneficial  -- fraction of beneficial mutations
            f_mutator     -- fraction of mutator mutations
            f_antimutator -- fraction of antimutator mutations
            f_lethal      -- fraction of lethal mutations out of the deleterious mutations
            M_deleterious -- average fitness effect of a deleterious mutation
            M_beneficial  -- average fitness effect of a beneficial mutation
            M_mutator     -- average fitness effect of a mutator mutation
            M_antimutator -- average fitness effect of a antimutator mutation
        """
        assert 0 < base_mut_rate <= 1
        assert 0 <= f_deleterious <= 1
        assert 0 <= f_beneficial <= 1
        assert 0 <= f_mutator <= 1
        assert 0 <= f_antimutator <= 1
        assert 0 <= f_lethal <= 1
        assert 0 <= f_antimutator + f_mutator + f_beneficial + f_deleterious + f_beneficial + f_mutator + f_antimutator <= 1
        assert 0 <= M_deleterious < 1
        assert M_beneficial >= 0
        assert M_mutator >= 0
        assert M_antimutator >= 0
        self.base_mut_rate  = base_mut_rate
        self.f_deleterious  = f_deleterious
        self.f_beneficial   = f_beneficial 
        self.f_mutator      = f_mutator    
        self.f_antimutator  = f_antimutator
        self.f_lethal       = f_lethal     
        self.M_deleterious  = M_deleterious
        self.M_beneficial   = M_beneficial 
        self.M_mutator      = M_mutator    
        self.M_antimutator  = M_antimutator        
        self.n_deleterious  = 0
        self.n_beneficial   = 0
        self.n_mutator      = 0
        self.n_antimutator  = 0
        self.n_lethal       = 0
        self.fitness        = 1
        self.mut_rate       = 1

    def add_deleterious(self, n_mutations):
        """
        Add a number of deleterious mutations and update fitness.

        Parameter:

            n_mutations -- number of mutations to add
        """
        self.n_deleterious += n_mutations
        for i in range(n_mutations):
            s = np.random.gamma(1, self.M_deleterious)
            # prevent 1 - s from going negative
            if s > 1:
                s = 1
            self.fitness *= 1 - s

    def add_beneficial(self, n_mutations):
        """
        Add a number of beneficial mutations and update fitness.

        Parameter:

            n_mutations -- number of mutations to add
        """
        self.n_beneficial += n_mutations
        for i in range(n_mutations):
            s = np.random.gamma(1, self.M_deleterious)
            self.fitness *= 1 + s

    def add_lethal(self, n_mutations):
        """
        Add a number of lethal mutations.

        Parameter:

            n_mutations -- number of mutations to add
        """
        self.n_lethal += n_mutations

    def add_mutator(self, n_mutations):
        """
        Add a number of mutator mutations and update relative mutation rate.

        Parameter:

            n_mutations -- number of mutations to add
        """
        self.n_mutator += n_mutations
        for i in range(n_mutations):
            s = np.power(np.random.random(), - self.M_mutator)
            self.mut_rate *= s

    def add_antimutator(self, n_mutations):
        """
        Add a number of antimutator mutations and update relative mutation rate.

        Parameter:

            n_mutations -- number of mutations to add
        """
        self.n_antimutator += n_mutations
        for i in range(n_mutations):
            s = np.power(np.random.random(), self.M_antimutator)
            self.mut_rate *= s

    def generate_offspring(self):
        """
        Generate an offspring of an Individual object allowing mutations to occur.
        """
        offspring = copy.deepcopy(self)
        offspring.add_lethal(np.random.poisson(self.base_mut_rate * self.mut_rate * self.f_deleterious * self.f_lethal))
        if self.n_lethal > 0:
            offspring.fitness = 0
            return offspring
        offspring.add_deleterious(np.random.poisson(self.base_mut_rate * self.mut_rate * self.f_deleterious))
        offspring.add_beneficial(np.random.poisson(self.base_mut_rate * self.mut_rate * self.f_beneficial))
        offspring.add_mutator(np.random.poisson(self.base_mut_rate * self.mut_rate * self.f_mutator))
        offspring.add_antimutator(np.random.poisson(self.base_mut_rate * self.mut_rate * self.f_antimutator))
        return offspring


class Population(object):
    """
    Define a population of Individuals.

    Attributes:

        mu       -- mutation rate of every individual in the population
        pop_size -- population size
        w        -- fitness of every individual in the population
    """

    def __init__(self, pop_size, base_mut_rate, f_deleterious, f_beneficial, f_mutator, f_antimutator, f_lethal, M_deleterious, M_beneficial, M_mutator, M_antimutator):
        """
        Generate a population of a given size and population genetic parameters.

        Parameters:

            pop_size      -- population size
            base_mut_rate -- base genomic mutation rate
            f_deleterious -- fraction of deleterious mutations
            f_beneficial  -- fraction of beneficial mutations
            f_mutator     -- fraction of mutator mutations
            f_antimutator -- fraction of antimutator mutations
            f_lethal      -- fraction of lethal mutations out of the deleterious mutations
            M_deleterious -- average fitness effect of a deleterious mutation
            M_beneficial  -- average fitness effect of a beneficial mutation
            M_mutator     -- average fitness effect of a mutator mutation
            M_antimutator -- average fitness effect of a antimutator mutation
        """
        self.population = []
        for i in range(pop_size):
            self.population.append(Individual(base_mut_rate, f_deleterious, f_beneficial, f_mutator, f_antimutator, f_lethal, M_deleterious, M_beneficial, M_mutator, M_antimutator))
        self.pop_size = pop_size

    def get_pop_fitness(self):
        """
        Calculate the fitness of every individual in the population.
        Return a np.array.
        """
        self.w = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.w[i] = self.population[i].fitness

    def get_pop_mut_rate(self):
        """
        Calculate the relative mutation rate of every individual in the population.
        Return a np.array.
        """
        self.mu = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.mu[i] = self.population[i].mut_rate

    def get_deleterious(self):
        """
        Calculate the number of deleterious mutations of every individual in the population.
        Return a np.array.
        """
        self.n_deleterious = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_deleterious[i] = self.population[i].n_deleterious

    def get_beneficial(self):
        """
        Calculate the number of beneficial mutations of every individual in the population.
        Return a np.array.
        """
        self.n_beneficial = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_beneficial[i] = self.population[i].n_beneficial

    def get_mutator(self):
        """
        Calculate the number of mutator mutations of every individual in the population.
        Return a np.array.
        """
        self.n_mutator = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_mutator[i] = self.population[i].n_mutator

    def get_antimutator(self):
        """
        Calculate the number of antimutator mutations of every individual in the population.
        Return a np.array.
        """
        self.n_antimutator = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_antimutator[i] = self.population[i].n_antimutator

    def get_next_generation(self):
        """
        Generate the following generation by sampling Individuals with replacement in proportion to their fitness and
        generating individual offspring from each until a certain size is reached.
        """
        next_generation = copy.deepcopy(self)
        next_generation.population = []
        self.get_pop_fitness()
        cum_fitness = np.cumsum(self.w)
        rand_array = np.random.random_sample(self.pop_size)
        mult_rand_array = np.multiply(rand_array, cum_fitness[self.pop_size - 1])
        indices = np.searchsorted(cum_fitness, mult_rand_array)
        for i in range(self.pop_size):
            next_generation.population.append(self.population[indices[i]].generate_offspring())
        return next_generation

    def get_stats(self):
        """
        Calculate summary statistics for the population.
        Return a dictionary.
        """
        self.get_pop_fitness()
        self.get_pop_mut_rate()
        self.get_deleterious()
        self.get_beneficial()
        self.get_mutator()
        self.get_antimutator()
        self.stats = {"mean_fitness": self.w.mean(), "var_fitness": self.w.var(), 
                      "mean_mut_rate": self.mu.mean(), "var_mut_rate": self.mu.var(), 
                      "mean_deleterious": self.n_deleterious.mean(), "var_deleterious": self.n_deleterious.var(), 
                      "mean_beneficial" : self.n_beneficial.mean(), "var_beneficial" : self.n_beneficial.var(), 
                      "mean_mutator"    : self.n_mutator.mean(), "var_mutator"    : self.n_mutator.var(), 
                      "mean_antimutator": self.n_antimutator.mean(), "var_antimutator": self.n_antimutator.var()}

    @staticmethod
    def write_data_to_file(population, file, gen):
        """Output summary statistics to file."""
        file.write(str(gen)                                  + "\t" +
                   str(population.stats["mean_fitness"])     + "\t" +
                   str(population.stats["mean_mut_rate"])    + "\t" +
                   str(population.stats["mean_deleterious"]) + "\t" +
                   str(population.stats["mean_beneficial"])  + "\t" +
                   str(population.stats["mean_mutator"])     + "\t" +
                   str(population.stats["mean_antimutator"]) + "\t" +
                   str(population.stats["var_fitness"])      + "\t" +
                   str(population.stats["var_mut_rate"])     + "\t" +
                   str(population.stats["var_deleterious"])  + "\t" +
                   str(population.stats["var_beneficial"])   + "\t" +
                   str(population.stats["var_mutator"])      + "\t" +
                   str(population.stats["var_antimutator"])  + "\n")

    @staticmethod
    def write_title_to_file(file):
        """Output title row to file"""
        file.write("generation"       + "\t" +
                   "mean_fitness"     + "\t" +
                   "mean_mut_rate"    + "\t" +
                   "mean_deleterious" + "\t" +
                   "mean_beneficial"  + "\t" +
                   "mean_mutator"     + "\t" +
                   "mean_antimutator" + "\t" +
                   "var_fitness"      + "\t" +
                   "var_mut_rate"     + "\t" +
                   "var_deleterious"  + "\t" +
                   "var_beneficial"   + "\t" +
                   "var_mutator"      + "\t" +
                   "var_antimutator"  + "\n")


class Evolution(object):
    """
    Simulate Evolution of a population.

    Attributes:

        curr_population -- Population in the current generation
    """
    def __init__(self, population, n_generations, replicate, period = 1, verbose = False, name = "simulation"):
        """
        Generate Evolution simulation from a Population.

        Parameters:

            population    -- Population object
            n_generations -- number of generations to run simulation
            replicate     -- replicate number of current simulation, part of the output file name to differentiate from other replicates
            period        -- number of generations between output of summary stats (default = 1)
            verbose       -- bool specifying whether to print generation number to the console (default = False)
            name          -- file name for output (default = "simulation")
        """
        gen = 0
        population.get_stats()
        self.curr_population = population
        file = open(str(replicate) + "_" + name + ".txt", "w")
        Population.write_title_to_file(file)
        Population.write_data_to_file(population, file, gen)
        for i in range(n_generations):
            next_generation = self.curr_population.get_next_generation()
            self.curr_population = next_generation
            gen += 1
            if gen % period == 0:
                next_generation.get_stats()
                Population.write_data_to_file(next_generation, file, gen)
                if verbose:
                    print gen
        file.close()


def main():
    """
    Parse parameters entered at command line and run evolutionary simulation.
    """
    parser = argparse.ArgumentParser(description="Wright-Fisher model for evolution of mutation rates in asexual populations")
    parser.add_argument("--pop_size", dest="pop_size", help="population size, default = 1000", type=int, default="1000")
    parser.add_argument("--mu", dest="mu", help="base mutation rate, default = 0.1", type=float, default="0.1")
    parser.add_argument("--fd", dest="fd", help="fractions of deleterious mutations, default = 0.5", type=float, default="0.5")
    parser.add_argument("--fb", dest="fb", help="fractions of beneficial mutations, default = 3e-4", type=float, default="3e-4")
    parser.add_argument("--fm", dest="fm", help="fractions of mutator mutations, default = 1e-3", type=float, default="1e-3")
    parser.add_argument("--fa", dest="fa", help="fractions of antimutator mutations, default = 1e-4", type=float, default="1e-4")
    parser.add_argument("--fl", dest="fl", help="fractions of lethal mutations, default = 0", type=float, default="0")
    parser.add_argument("--md", dest="md", help="the mean of deleterious mutation fitness effects, default = 0.03", type=float, default="0.03")
    parser.add_argument("--mb", dest="mb", help="the mean of beneficial mutation fitness effects, default = 0.03", type=float, default="0.03")
    parser.add_argument("--mm", dest="mm", help="the mean of mutators' effects on mutation rates, default = 0.03", type=float, default="0.03")
    parser.add_argument("--ma", dest="ma", help="the mean of antimutators' effects on mutation rates, default = 0.03", type=float, default="0.03")
    parser.add_argument("--gen", dest="n_gen", help="number of generations to evolve, default = 400,000", type=int, default="400000")
    parser.add_argument("--rep", dest="replicate", help="replicate number, default = 1", type=int, default="1")
    parser.add_argument("--period", dest="period", help="period to measure, default = 1", type=int, default="1")
    parser.add_argument("--verbose", dest="verbose", help="print out current generation if present", action="store_true")
    parser.add_argument("--name", dest="name", help="name of the running job, will be part of the output file name, default = simulation", type=str, default="simulation")
    args = parser.parse_args()

    Evolution(Population(args.pop_size, args.mu, args.fd, args.fb, args.fm, args.fa, args.fl, args.md, args.mb, args.mm, args.ma),
              args.n_gen, args.replicate, args.period, args.verbose, args.name)


if __name__ == "__main__":
    main()
