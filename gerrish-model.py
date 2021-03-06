# Release notes
# version 2.1
# 1. Implement the Gerrish model
# --------------------------------
# version 2.0
# 1. Write results to text file
# 2. Pass arguments from command line to specify certain parameters (type "python gerrish-model.py -h" for usage)


import numpy as np
import copy
import argparse

class Individual(object):
    """
    Define an individual as an object and with certain methods to add mutations and producing offspring
    """

    def __init__(self, base_mut_rate, f_deleterious, f_beneficial, f_mutator, f_antimutator, f_lethal, M_deleterious, M_beneficial, M_mutator, M_antimutator):
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
        self.base_mut_rate  = base_mut_rate
        self.fitness        = 1
        self.mut_rate       = 1

    def add_deleterious(self, n_mutations):
        self.n_deleterious += n_mutations
        for i in range(n_mutations):
            s = np.random.gamma(1, self.M_deleterious)
            if s > self.fitness:  # to prevent fitness from going negative
                self.fitness = 0
            else:
                self.fitness *= 1 - s

    def add_beneficial(self, n_mutations):
        self.n_beneficial += n_mutations
        for i in range(n_mutations):
            s = np.random.gamma(1, self.M_deleterious)
            self.fitness *= 1 + s

    def add_lethal(self, n_mutations):
        self.n_lethal += n_mutations

    def add_mutator(self, n_mutations):
        self.n_mutator += n_mutations
        for i in range(n_mutations):
            s = np.power(np.random.random(), - self.M_mutator)
            self.mut_rate *= s


    def add_antimutator(self, n_mutations):
        self.n_antimutator += n_mutations
        for i in range(n_mutations):
            s = np.power(np.random.random(), self.M_antimutator)
            self.mut_rate *= s

    def generate_offspring(self):
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

# <codecell>

class Population(object):
    """
    Define population as an object consisting of individuals
    """

    def __init__(self, init_pop_size, base_mut_rate, f_deleterious, f_beneficial, f_mutator, f_antimutator, f_lethal, M_deleterious, M_beneficial, M_mutator, M_antimutator):
        self.population = []
        for i in range(init_pop_size):
            self.population.append(Individual(base_mut_rate, f_deleterious, f_beneficial, f_mutator, f_antimutator, f_lethal, M_deleterious, M_beneficial, M_mutator, M_antimutator))
        self.get_pop_size()

    def get_pop_size(self):
        self.pop_size = len(self.population)

    def get_pop_fitness(self):
        self.get_pop_size()
        self.w = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.w[i] = self.population[i].fitness

    def get_pop_mut_rate(self):
        self.get_pop_size()
        self.mu = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.mu[i] = self.population[i].mut_rate

    def get_deleterious(self):
        self.get_pop_size()
        self.n_deleterious = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_deleterious[i] = self.population[i].n_deleterious


    def get_beneficial(self):
        self.get_pop_size()
        self.n_beneficial = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_beneficial[i] = self.population[i].n_beneficial

    def get_mutator(self):
        self.get_pop_size()
        self.n_mutator = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_mutator[i] = self.population[i].n_mutator

    def get_antimutator(self):
        self.get_pop_size()
        self.n_antimutator = np.zeros(self.pop_size)
        for i in range(self.pop_size):
            self.n_antimutator[i] = self.population[i].n_antimutator

    def get_next_generation(self, nudging_factor, init_pop_size):
        next_generation = copy.deepcopy(self)
        next_generation.population = []
        self.get_pop_size()
        self.get_pop_fitness()
        for i in range(self.pop_size):
#            print i, self.w.mean(), nudging_factor, init_pop_size, self.pop_size
            mean_offspring = (self.w[i] / self.w.mean()) * (1 + nudging_factor * (init_pop_size - self.pop_size) / init_pop_size)
#            print mean_offspring
            n_offspring = np.random.poisson(mean_offspring, 1)
            if len(next_generation.population) <= 2 * init_pop_size:
                for j in range(n_offspring):
                    next_generation.population.append(self.population[i].generate_offspring())
            else:
                break
        return next_generation

    def get_stats(self):
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
                      "mean_antimutator": self.n_antimutator.mean(), "var_antimutator": self.n_antimutator.var(),
                      "pop_size": self.pop_size}

# <codecell>

    @staticmethod
    def get_intervals(x):
        y = x / sum(x)
        intervals = {}
        sumy = 0
        for i in range(len(y)):
            next_sumy = sumy + y[i]
            intervals.update({i: (sumy, next_sumy)})
            sumy = next_sumy
        return intervals

    @staticmethod
    def write_data_to_file(population, file, gen):
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
                   str(population.stats["var_antimutator"])  + "\t" +
                   str(population.stats["pop_size"])         + "\n")

    @staticmethod
    def write_title_to_file(file):
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
                   "var_antimutator"  + "\t" +
                   "pop_size"         + "\n")


class Evolution(object):
    """
    This object manipulates the object population to simulate over generations.
    """
    def __init__(self, nudging_factor, init_pop_size, population, n_generations, replicate, period = 1, verbose = False, name = "simulation"):
        gen = 0
        population.get_stats()
        self.curr_population = population
        file = open('/project/worm/MutatorModel/' + str(replicate) + "_" + name + ".txt", "w")
        Population.write_title_to_file(file)
        Population.write_data_to_file(population, file, gen)
        for i in range(n_generations):
            next_generation = self.curr_population.get_next_generation(nudging_factor, init_pop_size)
            self.curr_population = next_generation
            gen += 1
            if gen % period == 0:
                next_generation.get_stats()
                Population.write_data_to_file(next_generation, file, gen)
                if verbose:
                    print "Current generation: ", gen
        file.close()


# Add command line arguments
# Type "python gerrish.sh -h" under command line for usage

parser = argparse.ArgumentParser(description="Gerrish model for evolution of mutation rates in asexual populations")
parser.add_argument("--init_pop_size", dest="init_pop_size", help="population size, default = 1000", type=int, default="1000")
parser.add_argument("--nudging", dest="nudging", help="nudging factor to keep population size approximately constant, default = 0.3", type=float, default="0.3")
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
parser.add_argument("--gen", dest="n_gen", help="number of generations to evolve, default = 60,000", type=int, default="60000")
parser.add_argument("--rep", dest="replicate", help="replicate number, default = 1", type=int, default="2")
parser.add_argument("--period", dest="period", help="period to measure, default = 1", type=int, default="1")
parser.add_argument("--verbose", dest="verbose", help="print out current generation if present", action="store_true")
parser.add_argument("--name", dest="name", help="name of the running job, will be part of the output file name, default = sims", type=str, default="sims")
args = parser.parse_args()

Evolution(args.nudging, args.init_pop_size, Population(args.init_pop_size, args.mu, args.fd, args.fb, args.fm, args.fa, args.fl, args.md, args.mb, args.mm, args.ma),
          args.n_gen, args.replicate, args.period, args.verbose, args.name)
