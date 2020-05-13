function result = SSA(func, dim, bound, max_iter, pop_size, r_a, p_c, p_m, info)

if (nargin == 0)
    func = @(x)sum(x.^2, 2);
    dim = 30;
    bound = 100;
    max_iter = 10000;
    pop_size = 25;
    r_a = 1;
    p_c = 0.7;
    p_m = 0.1;
    info = true;
end

g_best = Inf;
g_best_hist = [];
g_best_pos = zeros(1, dim);
position = rand(pop_size, dim) * 2 * bound - bound;
target_position = position;
target_intensity = zeros(pop_size, 1);
mask = zeros(pop_size, dim);
movement = zeros(pop_size, dim);
inactive = zeros(pop_size, 1);

if (info)
    fprintf('               SSA starts at %s\n', datestr(now));
    fprintf('==============================================================\n');
    fprintf(' iter    optimum    pop_min  base_dist  mean_dist time_elapsed\n');
    fprintf('==============================================================\n');
    start_time = java.lang.System.currentTimeMillis;
end
    
iter = 0;
while (iter < max_iter)
    iter = iter + 1;
    spider_fitness = func(position);
    base_distance = mean(std(position));
    distance = squareform(pdist(position));
    
    if (min(spider_fitness) < g_best)
        [g_best, ind] = min(spider_fitness);
        g_best_pos = position(ind, :);
    end
    g_best_hist = [g_best_hist g_best];
    if (info && (iter == 1 || iter == 10 ...
            || (iter < 1001 && mod(iter, 100) == 0) ...
            || (iter < 10001 && mod(iter, 1000) == 0) ...
            || (iter < 100000 && mod(iter, 1000) == 0)))
        elapsed_time = java.lang.System.currentTimeMillis - start_time;
        fprintf(['% 5s %.4e %.4e %.4e %.4e %02d:%02d:%02d.%03d\n'], num2str(iter), ...
            g_best, min(spider_fitness), base_distance, mean(mean(distance)), ...
            fix(elapsed_time / 3600000), mod(fix(elapsed_time / 60000), 60), ...
            mod(fix(elapsed_time / 1000), 60), (mod(elapsed_time, 1000)));
    end
    
    intensity_source = log(1 ./ (spider_fitness + 1E-100) + 1);
    intensity_attenuation = exp(-distance / (base_distance * r_a));
    intensity_receive = repmat(intensity_source, 1, pop_size) .* intensity_attenuation;

    [~, max_index] = max(intensity_receive, [], 2);
    best_receive = intensity_receive(sub2ind([pop_size, pop_size], 1:pop_size, max_index'))';
    keep_target = best_receive <= target_intensity;
    keep_target_matrix = repmat(keep_target, 1, dim);
    inactive = inactive .* keep_target + keep_target;
    target_intensity = target_intensity .* keep_target + best_receive .* (1 - keep_target);
    target_position = target_position .* keep_target_matrix + position(max_index, :) .* (1 - keep_target_matrix);

    rand_position = reshape(position(sub2ind([pop_size, dim], ceil(rand(1, pop_size * dim) * pop_size), ...
        reshape(repmat(1:dim, pop_size, 1), 1, pop_size * dim))), pop_size, dim);
    new_mask = ceil(rand(pop_size, dim) + rand() * p_m - 1);
    keep_mask = rand(pop_size, 1) < p_c.^inactive;
    inactive = inactive .* keep_mask;
    keep_mask_matrix = repmat(keep_mask, 1, dim);
    mask = keep_mask_matrix .* mask + (1 - keep_mask_matrix) .* new_mask;

    follow_position = mask .* rand_position + (1 - mask) .* target_position;
    movement = repmat(rand(pop_size, 1), 1, dim) .* movement + ...
        (follow_position - position) .* rand(pop_size, dim);
    position = position + movement;
end

if (info)
    fprintf('==============================================================\n');
    elapsed_time = java.lang.System.currentTimeMillis - start_time;
    fprintf(['% 5s %.4e %.4e %.4e %.4e %02d:%02d:%02d.%03d\n'], num2str(iter), ...
        g_best, min(spider_fitness), base_distance, mean(mean(distance)), ...
        fix(elapsed_time / 3600000), mod(fix(elapsed_time / 60000), 60), ...
        mod(fix(elapsed_time / 1000), 60), (mod(elapsed_time, 1000)));
    fprintf('==============================================================\n');
end
result.global_best_fitness = g_best;
result.global_best_solution = g_best_pos;
result.iterations = iter;

end