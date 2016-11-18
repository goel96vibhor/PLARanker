package NeuralNet;

import NeuralNet.TransferFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vibhor.go on 11/14/16.
*/

public class Neuron {

    private TransferFunction transferFunction;
    protected double output;
    protected ArrayList<Double> outputs= new ArrayList<Double>();
    protected List<Synapse> inLinks= new ArrayList<Synapse>();
    protected List<Synapse> outlinks= new ArrayList<Synapse>();
    private Double learningRate= 0.001;
    private Double delta_i;
    private Double delta_j[];

    public Neuron(){
        transferFunction= new TransferFunction();

    }

    public void computeOuterDeltas(ArrayList<ArrayList<Integer> > pairmap, ArrayList<ArrayList<Double> > pairweight, int current)
    {
        ArrayList<Integer> pairmap_i= pairmap.get(current);
        delta_j=new Double[pairmap_i.size()];
        double lambda;
        delta_i=0.0;
        double weight=0.0;
        for(int k=0;k<pairmap_i.size();k++)
        {
            int j=pairmap_i.get(k);
            if(pairweight==null)
            {
                weight=1.0;
                lambda=weight*(Double)(1.0/(1.0+Math.exp(outputs.get(current)-outputs.get(j))));

            }
            else
            {
                weight=pairweight.get(current).get(k);
                lambda=weight*(Double)(1.0/(1.0+Math.exp(outputs.get(current)-outputs.get(j))));
            }
            delta_i+=lambda;
            delta_j[k]=lambda*transferFunction.getDerivative(outputs.get(j));
        }
        delta_i*=transferFunction.getDerivative(outputs.get(current));
    }

    public void computeInnerDeltas(ArrayList<ArrayList<Integer> > pairmap, ArrayList<ArrayList<Double> > pairweight, int current)
    {
        ArrayList<Integer> pairmap_i= pairmap.get(current);
        delta_j=new Double[pairmap_i.size()];
        Arrays.fill(delta_j,0.0d);
        Double weight=1.0d;
        delta_i=0.0d;
        for(Synapse synapse:outlinks)
        {
            Neuron outerNeuron=synapse.outputNeuron;
            for(int k=0;k<pairmap_i.size();k++)
            {
                int j=pairmap_i.get(k);
                delta_j[k]+=outerNeuron.delta_j[k]*synapse.getWeight()*transferFunction.getDerivative(outputs.get(j));
            }
            delta_i+=outerNeuron.delta_i*synapse.getWeight()*transferFunction.getDerivative(outputs.get(current));
        }
    }

    public void updateWeight(ArrayList<ArrayList<Integer> > pairmap,int current)
    {
        Double errorsum=0.0d;
        for(Synapse synapse:inLinks)
        {
            for(int k=0;k<delta_j.length;k++)
            {
                errorsum+=delta_j[k]*synapse.inputNeuron.outputs.get(pairmap.get(current).get(k));
            }
            Double dw=delta_i*synapse.inputNeuron.outputs.get(current)-errorsum;
            dw*=learningRate;
            synapse.setWeightAdjustment(dw);
            synapse.adjustWeight();

        }
    }

    public void computeOutput(int index)
    {
        Double wsum=0.0d;
        for(Synapse synapse:inLinks)
        {
            //System.out.println(synapse.inputNeuron.outputs.size()+" "+index);
            wsum+=synapse.inputNeuron.outputs.get(index)*synapse.getWeight();
        }
        outputs.set(index,transferFunction.getValue(wsum));

    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public List<Double> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<Double> outputs) {
        this.outputs = outputs;
    }

    public Double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }

    public Double[] getDelta_j() {
        return delta_j;
    }

    public void setDelta_j(Double[] delta_j) {
        this.delta_j = delta_j;
    }

    public Double getDelta_i() {
        return delta_i;
    }

    public void setDelta_i(Double delta_i) {
        this.delta_i = delta_i;
    }
}