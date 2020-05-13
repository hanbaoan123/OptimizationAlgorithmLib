#include "SSA.h"

using namespace std;

class MyProblem : public Problem {
public:
    MyProblem(unsigned int dimension) : Problem(dimension) { }

    double eval(const std::vector<double>& solution) {
        double sum = 0.0;
        for (int i = 0; i < solution.size(); ++i) {
            sum += solution[i] * solution[i];
        }
        return sum;
    }
};

int main() {
    SSA ssa(new MyProblem(30), 30);
    ssa.run(10000, 1.0, 0.7, 0.1);
    return 0;
}