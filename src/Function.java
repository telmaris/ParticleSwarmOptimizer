import java.util.Vector;

public abstract class Function
{
    public abstract Result Return(Vector<Double> pos);

    public int GetDim()
    {
        return dim;
    }

    public int dim;
    public double min;
}

class Quadratic extends Function
{
    public Quadratic(double a, double b, double c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.dim = 1;

        double x = -b/(2*a);
        this.min = a*x*x + b*x + c;
    }

    public Result Return(Vector<Double> pos)
    {
        Result r = new Result();
        double x = pos.get(0);

        r.pos = pos;
        r.value = a*x*x + b*x + c;
        return r;
    }

    private double a, b, c;
}

class Bowl extends Function
{
    public Bowl()
    {
        this.dim = 2;
    }

    public Result Return(Vector<Double> pos)
    {
        Result r = new Result();
        double x = pos.get(0);
        double y = pos.get(1);

        r.pos = pos;
        r.value = x*x + y*y;
        return r;
    }
}

class Rosenbrock extends Function
{
    public Rosenbrock(double a, double b)
    {
        this.a = a;
        this.b = b;
        this.dim = 2;
        this.min = 0;
    }

    public Result Return(Vector<Double> pos)
    {
        Result r = new Result();
        double x = pos.get(0);
        double y = pos.get(1);

        r.pos = pos;
        r.value = Math.pow((a-x), 2) + Math.pow(b*(y - x*x), 2);
        return r;
    }

    private double a, b;
}
