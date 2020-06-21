import java.util.Arrays;

public class SplitByteArray {

    public static byte[][] splitByteArray(byte[] bytes){
        int byteLength = bytes.length;
        int numOfSplits = 3;
        int bytesPerSplit = byteLength/numOfSplits + 1;

        System.out.println(bytesPerSplit);

        byte[][] splitBytes = new byte[numOfSplits][bytesPerSplit];
        int index = 0;
        for(int i = 0; i < bytes.length; i += bytesPerSplit) {
            splitBytes[index] = Arrays.copyOfRange(bytes, i, Math.min(bytes.length, i + bytesPerSplit));
            index++;
        }
        return splitBytes;
    }

    public static byte[] combineByteArray(byte[][] splitBytes){
        //Find total number of bytes
        int len = 0;
        for(byte[] bts : splitBytes){
            for(byte b : bts){
                len++;
            }
        }

        //Init a new byte array
        byte[] newBytes = new byte[len];

        //Add all bytes to the array
        int i = 0;
        for(byte[] bts : splitBytes){
            for(byte b : bts){
                newBytes[i] = b;
                i++;
            }
        }
        return newBytes;
    }

}
