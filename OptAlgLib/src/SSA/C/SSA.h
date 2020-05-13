#ifndef SSA_SSA_H
#define SSA_SSA_H

#include <chrono>
#include <iostream>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <vector>

class Problem {
public:
    unsigned int dimension;

    Problem(unsigned int dimension) : dimension(dimension) { }
    virtual double eval(const std::vector<double>& solution) = 0;
};

class Position {
public:
    double fitness;
    std::vector<double> solution;

    Position() { };
    Position(const std::vector<double>& solution, double fitness) :
            solution(solution), fitness(fitness) { }

    friend bool operator==(const Position& p1, const Position& p2) {
        for (int i = 0; i < p1.solution.size(); ++i) {
            if (p1.solution[i] != p2.solution[i]) {
                return false;
            }
        }
        return true;
    }

    friend double operator-(const Position& p1, const Position& p2) {
        double distance = 0.0;
        for (int i = 0; i < p1.solution.size(); ++i) {
            distance += fabs(p1.solution[i] - p2.solution[i]);
        }
        return distance;
    }

    static Position init_position(Problem* problem);
};

class Vibration {
public:
    double intensity;
    Position position;
    static double C;

    Vibration() { }
    Vibration(const Position& position);
    Vibration(double intensity, const Position& position);

    double intensity_attenuation(double attenuation_factor, double distance) const;
    static double fitness_to_intensity(double fitness);
};

class Spider {
public:
    Position position;
    Vibration target_vibr;
    std::vector<bool> dimension_mask;
    std::vector<double> previous_move;
    int inactive_deg;

    Spider(const Position& position);

    void choose_vibration(const std::vector<Vibration>& vibrations,
            const std::vector<double>& distances, double attenuation_factor);
    void mask_changing(double p_change, double p_mask);
    void random_walk(const std::vector<Vibration>& vibrations);
};

class SSA {
public:
    Problem* problem;
    unsigned int dimension;
    std::vector<Spider> population;
    std::vector<Vibration> vibrations;
    std::vector<std::vector<double>> distances;
    Position global_best;

    SSA(Problem* problem, unsigned int pop_size);

    void run(int max_iteration, double attenuation_rate, double p_change, double p_mask);
    void fitness_calculation();
    void vibration_generation(double attenuation_rate);

private:
    int iteration;
    double population_best_fitness;
    double attenuation_base;
    double mean_distance;
    std::chrono::high_resolution_clock::time_point start_time;
    void print_header();
    void print_content();
    void print_footer();
};

inline double mean(std::vector<double> data) {
    double sum = 0.0;
    for (int i = 0; i < data.size(); ++i) {
        sum += data[i];
    }
    return sum / data.size();
}

inline double std_dev(std::vector<double> data) {
    double mean_val = mean(data);
    double sum = 0.0;
    for (int i = 0; i < data.size(); ++i) {
        sum += (mean_val - data[i]) * (mean_val - data[i]);
    }
    return sqrt(sum / data.size());
}

inline double randu() {
    return (double) rand() / (RAND_MAX + 1.0f);
}

std::string get_time_string();
std::string get_time_string(std::chrono::milliseconds ms);

#endif