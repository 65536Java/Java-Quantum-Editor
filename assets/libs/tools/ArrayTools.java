package assets.libs.tools;

public class ArrayTools {
    public static int search(Object[] arr, Object element){
        int counter = 0;
        for(Object s:arr){
            if (s == element){
                return counter;
            }
            counter++;
        }
        return -1;
    }
}
