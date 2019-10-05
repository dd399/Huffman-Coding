//Dhwanil Desai 7555 PRP CS610 Huffman Algo

import java.io.*;
import java.util.Arrays;

public class hdec7555 {

    static int[] Char_count =new int[256];
    static byte[] data;
    static Node7555 root;
    static int Required_space;


    //reading the encoded file
    static  void reading_file(String[] args) throws IOException {
        File file = new File(args[0]);
        FileInputStream filename = new FileInputStream(file);
        data= new byte[(int)file.length()];
        try {
            filename.read(data);
            filename.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static int get_char_freq(){
        int Null_indexCount=0;
        for (int i=0;i<data.length-3;i++){
            if (data[i]==0 &&(data[i+1]==0 && data[i+2]==0)){
                Null_indexCount=i;
                break;
            }
        }
        byte [] freq_info= Arrays.copyOfRange(data,0,Null_indexCount);
        String frequency_string_encoded_file=new String(freq_info);
        String[] characters_value=frequency_string_encoded_file.split(" ");
        for (int i=0;i<characters_value.length;i++){
            String[] character_freq=characters_value[i].split("\\*");
            Char_count[128+Integer.parseInt(character_freq[0])]=Integer.parseInt(character_freq[1]);
        }
        return Null_indexCount+3;
    }

    //building the huffman tree.
    static Node7555 build_tree(int[] freq_count){
        queue_7555 huff_queue=new queue_7555(freq_count.length);
        for(int i=0;i<freq_count.length;i++){
            if (freq_count[i]>0){
                Required_space +=freq_count[i];
                huff_queue.add(new Node7555((byte) (i -128),freq_count[i]));
            }
        }

        while(huff_queue.size()>1){
            Node7555 char1=huff_queue.remove();
            Node7555 char2=huff_queue.remove();
            Node7555 root=new Node7555((byte)0,char1.freq+char2.freq);
            root.left=char2;
            root.right=char1;
            huff_queue.add(root);
        }
        return huff_queue.remove();
    }

    //Check if the node is leaf or not
    static boolean check_leaf(Node7555 root){
        if (root.left==null && root.right==null){
            return true;
        }
        return false;
    }

    public static void main(String [] args) throws IOException {
        if (args.length!=1){
            System.out.println("Wrong arguments in input");
            return;
        }
        reading_file(args);
        int starting_position_of_data= get_char_freq();
        root= build_tree(Char_count);
        int bit=0;
        Node7555 root1=root;
        String given_file=args[0];
        String file_name=given_file.split(".huf")[0];
        OutputStream os = new FileOutputStream(file_name);
        DataOutputStream out=new DataOutputStream(os);
        int no_of_char=0;
        byte[] dump_array=new byte[Required_space];
        read_7555 br=new read_7555(Arrays.copyOfRange(data,starting_position_of_data,data.length));
        while ((bit=br.next())!=-1){
            if (bit==0){
                root1=root1.left;
            }
            else{
                root1=root1.right;
            }
            if (check_leaf(root1)){
                dump_array[no_of_char]=root1.character;
                no_of_char++;
                root1=root;
            }
        }
        out.write(dump_array);
        System.out.println("File size after decoding in bytes "+ Required_space);

    }

   static class queue_7555 {

        private Node7555[] heap;
        private  int current_heap_size;
        private  int heap_size;

        public queue_7555(int capacity){
            this.heap_size=capacity+1;
            heap=new Node7555[heap_size];
            this.current_heap_size=0;
        }

        public void add(Node7555 node){

            heap[++current_heap_size]=node;
            int position=current_heap_size;
            while(position!=1&&node.freq<heap[position/2].freq){
                heap[position]=heap[position/2];
                position/=2;
            }
            heap[position]=node;
        }
        public int size()
        {
            return current_heap_size;
        }

        public boolean isEmpty()
        {
            return current_heap_size == 0;
        }

        public Node7555 remove(){
            int parent, child;
            Node7555 item, temp;
            if (isEmpty() )
            {
                System.out.println("Heap is empty");
                return null;
            }

            item=heap[1];
            temp=heap[current_heap_size--];

            parent=1;
            child=2;
            while (child<=current_heap_size){
                if (child < current_heap_size && heap[child].freq > heap[child + 1].freq)
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

    static class read_7555 {

        byte[] input;
        int array_length;
        int curr_index;
        int current;
        int last;

        read_7555(byte[] input){
            this.input=input;
            this.array_length =input.length;
            this.curr_index =0;
            this.current =7;

            if (input[array_length -1]==8){
                last =7;
            }
            else{
                last =input[array_length -1]-1;
            }
        }

        int next(){
            if (curr_index >= array_length -2){
                if (curr_index == array_length -1){
                    return -1;
                }
                if(last ==0){
                    int ans=(input[curr_index] & (1 << last));
                    curr_index +=1;
                    return ans;
                }else {
                    int ans=(input[curr_index] & (1 << last));
                    last -=1;
                    return ans;
                }
            }
            if(current ==0){
                int ans=(input[curr_index] & (1 << current));
                current =7;
                curr_index +=1;
                return ans;
            }else {
                int ans=(input[curr_index] & (1 << current));
                current -=1;
                return ans;
            }
        }



    }







}
