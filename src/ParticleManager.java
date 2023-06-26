import java.util.*;

class Result
{
    public Result()
    {
        pos = new Vector<Double>();
        value = 0;
    }

    public Vector<Double> pos;
    public double value;
}

class Config
{
    int epochs;
    int swarmSize;
    int dim;

    final double dt = 0.01;
}

class Swarm
{
    public Swarm(int size, int dim)
    {
        swarm = new Vector<Particle>();

        for (int i = 0; i < size; i++)
        {
            Particle p = new Particle();
            p.SetID(i);
            p.SetPosition(GenerateVector(-10, 10, dim));
            p.SetVelocity(GenerateVector(-2, 2, dim));
            swarm.add(p);
            //System.out.println("A particle has been created. Position: " + p.GetPosition());
        }

        //System.out.println("A swarm has been created. Swarm size: " + swarm.size());
        //System.out.println("Swarm dimensions: " + dim);
    }

    public int GetSwarmSize()
    {
        return swarm.size();
    }

    public Vector<Particle> GetSwarm()
    {
        return swarm;
    }

    private Vector<Double> GenerateVector(double min, double max, int dim)
    {
        Vector<Double> vec = new Vector<Double>();

        Random rand = new Random();

        for (int i = 0; i < dim; i++)
        {
            vec.add(rand.nextDouble() * (max - min) + min);
        }

        return vec;
    }

    private Vector<Particle> swarm;
}

class Min
{
    public Min()
    {
        min = new Result();
    }

    public Result GetMin()
    {
        return min;
    }

    public void SetMin(Result m)
    {
        min = m;
    }
    
    private Result min;
}

public class ParticleManager
{
    public ParticleManager()
    {
        config = new Config();
        min = new Min();
        solver = new Solver();
        bench = new Benchmark();
    }

    public void CreateSwarm(int size)
    {
        swarm = new Swarm(size, config.dim);
        config.swarmSize = size;
    }

    public void SetEpochs(int num)
    {
        config.epochs = num;
    }

    public void AttachFunction(Function f)
    {
        func = f;
        config.dim = func.GetDim();
    }

    public Result Optimize()
    {
        solver.Run();


        return min.GetMin();
    }

    public void AttachBenchConfig(int testQuantity, int epochs, int swarmSize)
    {
        bench.quantity = testQuantity;
        bench.epochs = epochs;
        bench.swarmSize = swarmSize;
    }

    public void StartBenchmark()
    {
        bench.StartBenchmark();
    }

    private Function func;
    private Config config;
    private Swarm swarm;
    private Min min;
    private Solver solver;
    private Benchmark bench;

    private class Solver
    {
        public long Run()
        {
            //System.out.println("Solver starts. Swarm size: " + swarm.GetSwarmSize());

            long startTime = System.currentTimeMillis();

            results.clear();

            for(int e = 0; e < config.epochs; e++)
            {
                Calculate();
                FindMinimum();
                Adjust(e);
                //System.out.println("Epoch " + e);
                //System.out.println("value: " + min.GetMin().value);
                //System.out.println("position: " + min.GetMin().pos);
            }
            
            long endTime = System.currentTimeMillis();

            return endTime - startTime;

            //System.out.println("Solver finished. Minimal value: " + min.GetMin().value);
            //System.out.println("Solver finished. Optimal position: " + min.GetMin().pos);
        }

        private void Calculate()
        {
            for (Particle p : swarm.GetSwarm()) 
            {
                results.add(func.Return(p.GetPosition()));
            }
        }

        private void FindMinimum()
        {
            Vector<Double> vals = new Vector<Double>();

            for (Result r : results)
            {
                vals.add(r.value);
            }

            int index = vals.indexOf(Collections.min(vals));
            min.SetMin(results.get(index));
        }

        private void Adjust(int ratio)
        {
            for (Particle p : swarm.GetSwarm()) 
            {
                Vector<Double> v = p.GetVelocity();
                Vector<Double> pos = p.GetPosition();
                
                Vector<Double> buf = new Vector<Double>();
                
                //System.out.println("Old value: " + pos.get(0));

                for (int i = 0; i < p.GetPosition().size(); i++)
                {
                    double dp = min.GetMin().pos.get(i) - p.GetPosition().get(i);
                    double temp;
                    if(dp == 0)
                    {
                        temp = 0.8*v.get(i);
                    }
                    else
                    {
                        temp = v.get(i) + Sigmoid(ratio)*3/dp;
                    }
                    
                    buf.add(pos.get(i) + temp*config.dt);
                }
                
                //System.out.println("New value: " + buf.get(0));
                p.SetPosition(buf);
            }
        }

        private double Sigmoid(int epoch)
        {
            return 1 - 1/(1 + Math.exp(-1*(epoch/config.epochs - config.epochs/2)));
        }

        Vector<Result> results = new Vector<Result>();
    }

    private class Benchmark
    {
        public Benchmark()
        {

        }

        public void StartBenchmark()
        {
            SetEpochs(epochs);

            Vector<Long> timeMeasurements = new Vector<Long>();
            Vector<Double> error = new Vector<Double>();
            //Vector<Double> relativeError = new Vector<Double>();

            double totalTime = 0;
            double totalError = 0;

            System.out.println("=================================");
            System.out.print("Benchmark starting... [");

            for(int i = 0; i < quantity; i++)
            {
                CreateSwarm(swarmSize);

                long time = solver.Run();
                timeMeasurements.add(time);
                totalTime += time;
                totalError += func.min - min.GetMin().value;
                error.add(func.min - min.GetMin().value);
                //relativeError.add((func.min - min.GetMin().value)/min.GetMin().value);

                if((int)(((double)i/quantity)*10) > (int)((((double)i - 1)/quantity)*10))
                {
                    System.out.print("-");
                }
            }

            System.out.println("]");

            System.out.println("=================================");
            System.out.println("Benchmark finished...");
            System.out.println("=================================");
            System.out.println("Total time: " + totalTime + "ms");
            System.out.println("Average time: " + totalTime/quantity + "ms");
            System.out.println("Average error: " + totalError/quantity);

        }

        int quantity, epochs, swarmSize;
    }
}

