//Dhwanil Desai 7555 PRP CS610 henc7555

import java.io.*;
import java.util.Arrays;

public class henc7555 {
    static int [] Char_Count =new int[256];
    static Node7555 root;
    static  int Required_space =0;
    static byte [] data;
    static String[] codes=new String[256];
    static String[] argus;

    static  void reading_file(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        FileInputStream input_file = new FileInputStream(file);
        data= new byte[(int)file.length()];

        try {

            System.out.println("Current size of the file in bytes "+ data.length);
            input_file.read(data);
            input_file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //building the huffman tree.
    static Node7555 build_tree(int[] freq_count){
        Queue huff_queue=new Queue(freq_count.length);
        for(int i=0;i<freq_count.length;i++){
            if (freq_count[i]>0){
                huff_queue.add(new Node7555((byte)(i-128),freq_count[i]));
            }
        }

        while(huff_queue.size()>1){
            Node7555 char1=huff_queue.remove();
            Node7555 char2=huff_queue.remove();
            Node7555 root=new Node7555(null,char1.freq+char2.freq);
            root.left=char2;
            root.right=char1;
            huff_queue.add(root);
        }
        return huff_queue.remove();
    }

    static void generate_code(Node7555 root, String code){
        if(root==null){
            return;
        }
        generate_code(root.left,code+"0");
        generate_code(root.right,code+"1");
        root.code=code;
        if(root.character!=null){
            codes[128+root.character]=root.code;
        }
    }

    static void encode_File() throws IOException {
        String temp="";
        for(int i = 0; i< Char_Count.length; i++){
            if(Char_Count[i]>0){
                Required_space += Char_Count[i]*codes[i].length();
                temp+=Integer.toString(i-128)+"*"+Integer.toString(Char_Count[i])+" ";
            }
        }
        byte[] gg=new byte[temp.getBytes().length+3];
        System.arraycopy(temp.getBytes(),0,gg,0,gg.length-3);

        try (FileOutputStream fos = new FileOutputStream(argus[0]+".huf")) {
            fos.write(gg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStream output_stream = new FileOutputStream(argus[0]+".huf",true);
        DataOutputStream out=new DataOutputStream(output_stream);

        int data_count=0;
        int count=0;
        String data_length="";
        byte[] array_dump=new byte[(int)(Math.ceil((Required_space /8.0)))+1];
        while (data_count<data.length){
            data_length+=codes[128+data[data_count]];
            while (data_length.length()>8){
                byte newByte=(byte)Integer.parseInt(data_length.subSequence(0,8).toString(),2);;
                array_dump[count]=newByte;
                data_length=data_length.substring(8);
                count++;
            }
            data_count++;
        }
        byte newByte=Byte.parseByte(data_length, 2);
        array_dump[count]=newByte;
        count++;

        System.out.println("number of bytes of the compressed file "+count);
        System.out.println("Total Number of bits require "+ Required_space);

        if (data_length.length()==8){
            newByte=8;
            array_dump[count]=newByte;
        }
        else {
            newByte=(byte) data_length.length();
            array_dump[count]=newByte;
        }
        out.write(array_dump);
        out.close();
        output_stream.close();
    }


    public static void main(String[] args) throws IOException {
        if (args.length!=1){
            System.out.println("Wrong Input arguments");
            return;
        }
        argus=args;
        reading_file(args);
        Arrays.fill(Char_Count,0);

        for(int i=0;i<data.length;i++){
            Char_Count[128+data[i]]++;
        }
        root= build_tree(Char_Count);
        generate_code(root,"");
        encode_File();
    }



}

class Node7555 {
    Byte character;
    int freq;
    String code;
    Node7555 left;
    Node7555 right;

    public Node7555(Byte arg1, int arg2){
        this.freq=arg2;
        this.character=arg1;
        this.left=null;
        this.right=null;
    }

}

class Queue {

    private Node7555[] heap;
    private  int curr_heap_size;
    private  int heap_size;

    public Queue(int capacity){
        this.heap_size=capacity+1;
        heap=new Node7555[heap_size];
        this.curr_heap_size =0;
    }

    public void add(Node7555 node){

        heap[++curr_heap_size]=node;
        int position= curr_heap_size;
        while(position!=1&&node.freq<heap[position/2].freq){
            heap[position]=heap[position/2];
            position/=2;
        }
        heap[position]=node;
    }
    public int size()
    {
        return curr_heap_size;
    }

    public boolean isEmpty()
    {
        return curr_heap_size == 0;
    }

    public Node7555 remove(){
        int parent, child;
        Node7555 item, temp;
        if (isEmpty() )
        {
            System.out.println("Empty Heap");
            return null;
        }

        item=heap[1];
        temp=heap[curr_heap_size--];

        parent=1;
        child=2;
        while (child<= curr_heap_size){
            if (child < curr_heap_size && heap[child].freq > heap[child + 1].freq)
                child++;
            if (temp.freq <= heap[child].freq)
                break;

            heap[parent] = heap[child];
            parent = child;
            child *= 2;
        }
        heap[parent]=temp;
        return  item;
    }




}

