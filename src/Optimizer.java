public class Optimizer
{
    public Optimizer()
    {
        particles = new ParticleManager();

        //particles.AttachFunction(new Quadratic(3, -2, -5));
        particles.AttachFunction(new Rosenbrock(1, 100));
        particles.AttachBenchConfig(10, 100, 1000);
    }

    public void Run()
    {
        System.out.println("Optimizer starts now...");
        Result res = particles.Optimize();
        System.out.println("Solver finished. Minimal value: " + res.value);
        System.out.println("Solver finished. Optimal position: " + res.pos);
    }

    public void StartBenchmark()
    {
        particles.StartBenchmark();
    }

    private ParticleManager particles;
}