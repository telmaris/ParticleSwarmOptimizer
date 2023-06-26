import java.util.Vector;

class Particle
{
    public Particle()
    {
        position = new Vector<Double>();
        velocity = new Vector<Double>();
        id = 0;
    }

    public void SetID(int i)
    {
        id = i;
    }

    public Vector<Double> GetPosition()
    {
        return position;
    }

    public void SetPosition(Vector<Double> pos)
    {
        position = pos;
    }

    public Vector<Double> GetVelocity()
    {
        return velocity;
    }

    public void SetVelocity(Vector<Double> vel)
    {
        velocity = vel;
    }

    private Vector<Double> position;
    private Vector<Double> velocity;
    public int id;
}