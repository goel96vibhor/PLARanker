package NeuralNet;


/**
 * Created by vibhor.go on 11/14/16.
 */

public class TransferFunction {

    public Double getValue(Double input)
    {
        return (Double)(1.0/(1.0+Math.exp(-input)));
    }

    public Double getDerivative(Double input)
    {
        Double value= (Double)(1.0/(1.0+Math.exp(-input)));
        return value*(1.0-value);
    }

}