package Utils;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by vibhor.go on 11/17/16.
 */

public class ArrayIndexComparator implements Comparator<Integer>
{
    private final ArrayList<Integer> array;

    public ArrayIndexComparator(ArrayList<Integer> arrayList)
    {

        this.array = arrayList;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.size()];
        for (int i = 0; i < array.size(); i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
        // Autounbox from Integer to int to use as array indexes
        return array.get(index2).compareTo(array.get(index1));
    }
}