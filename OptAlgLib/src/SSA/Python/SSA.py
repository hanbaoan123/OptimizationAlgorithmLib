from __future__ import division
from __future__ import print_function
import sys

if sys.version.startswith('3'):
    xrange = range
import numpy as np

rand = np.random.rand
from scipy.spatial.distance import pdist, squareform


def problem(x):
    return np.sum(x ** 2, 1)


class SSA(object):
    def __init__(self, func: object,
                 dim: object = 30,
                 bound: object = 100,
                 max_iteration: object = 10000,
                 pop_size: object = 25,
                 r_a: object = 1,
                 p_c: object = 0.7,
                 p_m: object = 0.1) -> object:
        self.func = func
        self.dim = dim
        self.bound = bound
        self.max_iteration = max_iteration
        self.pop_size = pop_size
        self.r_a = r_a
        self.p_c = p_c
        self.p_m = p_m

    def run(self, show_info=False):
        self.g_best = np.Inf
        self.g_best_hist = []
        self.g_best_pos = np.zeros(self.dim)
        self.position = rand(self.pop_size, self.dim) * 2 * self.bound - self.bound
        target_position = self.position.copy()
        target_intensity = np.zeros(self.pop_size)
        mask = np.zeros((self.pop_size, self.dim))
        movement = np.zeros((self.pop_size, self.dim))
        inactive = np.zeros(self.pop_size)

        if show_info:
            import datetime, time
            print(" " * 15 + "SSA starts at " + datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
            print("=" * 62)
            print(" iter    optimum    pop_min  base_dist  mean_dist time_elapsed")
            print("=" * 62)
            self.start_time = time.time()

        iteration = 0
        while (iteration < self.max_iteration):
            iteration += 1
            spider_fitness = self.func(self.position)
            base_distance = np.mean(np.std(self.position, 0))
            distance = squareform(pdist(self.position, 'cityblock'))

            if np.min(spider_fitness) < self.g_best:
                self.g_best = np.min(spider_fitness)
                self.g_best_pos = self.position[np.argmin(spider_fitness)].copy()
            self.g_best_hist.append(self.g_best)
            if show_info and (iteration == 1 or iteration == 10
                              or (iteration < 1001 and iteration % 100 == 0)
                              or (iteration < 10001 and iteration % 1000 == 0)
                              or (iteration < 100000 and iteration % 10000 == 0)):
                elapsed_time = time.time() - self.start_time
                print(repr(iteration).rjust(5), "%.4e" % self.g_best, "%.4e" % np.min(spider_fitness),
                      "%.4e" % base_distance, "%.4e" % np.mean(distance),
                      "%02d:%02d:%02d.%03d" % (elapsed_time // 3600, elapsed_time // 60 % 60,
                                               elapsed_time % 60, (elapsed_time % 1) * 1000))

            intensity_source = np.log(1. / (spider_fitness + 1E-100) + 1)
            intensity_attenuation = np.exp(-distance / (base_distance * self.r_a))
            intensity_receive = np.tile(intensity_source, self.pop_size).reshape(self.pop_size,
                                                                                 self.pop_size) * intensity_attenuation

            max_index = np.argmax(intensity_receive, axis=1)
            keep_target = intensity_receive[np.arange(self.pop_size), max_index] <= target_intensity
            keep_target_matrix = np.repeat(keep_target, self.dim).reshape(self.pop_size, self.dim)
            inactive = inactive * keep_target + keep_target
            target_intensity = target_intensity * keep_target + intensity_receive[
                np.arange(self.pop_size), max_index] * (1 - keep_target)
            target_position = target_position * keep_target_matrix + self.position[max_index] * (1 - keep_target_matrix)

            rand_position = self.position[np.floor(rand(self.pop_size * self.dim) * self.pop_size).astype(int), \
                                          np.tile(np.arange(self.dim), self.pop_size)].reshape(self.pop_size, self.dim)
            new_mask = np.ceil(rand(self.pop_size, self.dim) + rand() * self.p_m - 1)
            keep_mask = rand(self.pop_size) < self.p_c ** inactive
            inactive = inactive * keep_mask
            keep_mask_matrix = np.repeat(keep_mask, self.dim).reshape(self.pop_size, self.dim)
            mask = keep_mask_matrix * mask + (1 - keep_mask_matrix) * new_mask

            follow_position = mask * rand_position + (1 - mask) * target_position
            movement = np.repeat(rand(self.pop_size), self.dim).reshape(self.pop_size, self.dim) * movement + \
                       (follow_position - self.position) * rand(self.pop_size, self.dim)
            self.position = self.position + movement

        if show_info:
            elapsed_time = time.time() - self.start_time
            print("=" * 62)
            print(repr(iteration).rjust(5), "%.4e" % self.g_best, "%.4e" % np.min(spider_fitness),
                  "%.4e" % base_distance, "%.4e" % np.mean(distance),
                  "%02d:%02d:%02d.%03d" % (elapsed_time // 3600, elapsed_time // 60 % 60,
                                           elapsed_time % 60, (elapsed_time % 1) * 1000))
            print("=" * 62)
        return {'global_best_fitness': self.g_best,
                'global_best_solution': self.g_best_pos,
                'iterations': iteration + 1}


if __name__ == '__main__':
    SSA(problem).run(True)