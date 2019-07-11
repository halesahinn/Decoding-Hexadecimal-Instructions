import java.util.Scanner;
import java.io.File;

public class HaleSahin_150116841 {
  

	public static void main(String[] args) throws Exception{
	//read from txt file
		try {
			File file = new File("inn.txt");
			Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.next();
                
                System.out.println(getResult(line) + "\n");
            }
            sc.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
		

	}
	            
	    public static String getResult(String s){
	    boolean[] ir= new boolean[16];	//store instruction in boolean array length 16 
		String IR = s;
	     String sum="";
		 boolean numberofbits=(IR.length()==16);		//Controlling entered number of bits
	    		if(numberofbits){
	    			for(int i=0;i<IR.length();i++){		//"1" represents with true , "0" =false
	    				if(IR.charAt(i)=='1')
	    					ir[i]=true;
	    				else
	    					ir[i]=false;			
	    			}
	    		}
		boolean AND =(!ir[0]&&ir[1]&&!ir[2]&&ir[3]);
		boolean ADD =(!ir[0]&&!ir[1]&&!ir[2]&&ir[3]);
		boolean BR =(!ir[0]&&!ir[1]&&!ir[2]&&!ir[3]);
		boolean JMP =(ir[0]&&ir[1]&&!ir[2]&&!ir[3]);		//this part is some kind of decoding 
		boolean LD =(!ir[0]&&!ir[1]&&ir[2]&&!ir[3]);		//only one of them will be "true"
		boolean LDI =(ir[0]&&!ir[1]&&ir[2]&&!ir[3]);
		boolean LDR =(!ir[0]&&ir[1]&&ir[2]&&!ir[3]);
		boolean LEA =(ir[0]&&ir[1]&&ir[2]&&!ir[3]);
		boolean NOT =(ir[0]&&!ir[1]&&!ir[2]&&ir[3]);
		boolean ST =(!ir[0]&&!ir[1]&&ir[2]&&ir[3]);
		boolean STI =(ir[0]&&!ir[1]&&ir[2]&&ir[3]);
		boolean STR =(!ir[0]&&ir[1]&&ir[2]&&ir[3]);	
		boolean[] offset9 ={ir[7],ir[8],ir[9],ir[10],ir[11],ir[12],ir[13],ir[14],ir[15]};
		boolean[] offset6 ={ir[10],ir[11],ir[12],ir[13],ir[14],ir[15]};
		boolean[] imm5 ={ir[11],ir[12],ir[13],ir[14],ir[15]};
		
		if(AND){
			sum="AND "+Rdecoder3bit(ir[4],ir[5],ir[6])+" "+Rdecoder3bit(ir[7],ir[8],ir[9]);
			if(ir[10]) //checking immediate or not
				sum+=" #"+toDecimal(imm5);
			else
				sum+=" "+Rdecoder3bit(ir[13],ir[14],ir[15]);
		}
		
		if(ADD){
			sum="ADD "+Rdecoder3bit(ir[4],ir[5],ir[6])+" "+Rdecoder3bit(ir[7],ir[8],ir[9]);
			if(ir[10])//checking immediate or not
				sum+=" #"+toDecimal(imm5);
			else
				sum+=" "+Rdecoder3bit(ir[13],ir[14],ir[15]);
		}
		
		if(BR){
			sum="BR";
			if(ir[4])
				sum+="n";
			if(ir[5])
				sum+="z";
			if(ir[6])
				sum+="p";	
			sum+=" "+"x"+toHex(offset9);
		}
		if(JMP){
			sum="JMP "+Rdecoder3bit(ir[7],ir[8],ir[9]);
		}
		if(LD){												//when  calculating address increasedPC+offset in decimal
			sum="LD "+Rdecoder3bit(ir[4],ir[5],ir[6])+("x"+toHex(offset9));
		}
		if(LDI){
			sum="LDI "+Rdecoder3bit(ir[4],ir[5],ir[6])+("x"+toHex(offset9));
		}
		if(LDR){
			sum="LDR "+Rdecoder3bit(ir[4],ir[5],ir[6])+" "+Rdecoder3bit(ir[7],ir[8],ir[9])+" "+"#"+toDecimal(offset6);
		}
		if(LEA){
			sum="LEA "+Rdecoder3bit(ir[4],ir[5],ir[6])+(" x"+toHex(offset9));
		}
		if(NOT){
			sum="NOT "+Rdecoder3bit(ir[4],ir[5],ir[6])+Rdecoder3bit(ir[7],ir[8],ir[9]);
		}
		if(ST){
			sum="ST "+Rdecoder3bit(ir[4],ir[5],ir[6])+("x"+toHex(offset9));
		}
		if(STI){
			sum="STI "+Rdecoder3bit(ir[4],ir[5],ir[6])+("x"+toHex(offset9));
		}
		if(STR){
			sum="STR "+Rdecoder3bit(ir[4],ir[5],ir[6])+Rdecoder3bit(ir[7],ir[8],ir[9])+"+ #"+toDecimal(offset6);
		}
		if(!(ADD||BR||JMP||LD||LDI||LDR||LEA||NOT||ST||STR||STI||STR||AND)){//in case of instructions that does not exist
			sum="instruction is not found";
		}
		for(int k=0;k<IR.length();k++){
			if(!((IR.charAt(k)=='1')||(IR.charAt(k)=='0'))||(!numberofbits))//checking correction of input
				sum="incorrect input!";
		}
		IR = "";
		return sum;
	}
	
	public static String Rdecoder3bit(boolean a,boolean b,boolean c){
		String sum="";
		boolean R0 = (!a&&!b&&!c);
		boolean R1 = (!a&&!b&&c);
		boolean R2 = (!a&&b&&!c);
		boolean R3 = (!a&&b&&c); //decoding 
		boolean R4 = (a&&!b&&!c);
		boolean R5 = (a&&!b&&c);
		boolean R6 = (a&&b&&!c);
		boolean R7 = (a&&b&&c);
		if(R0)
			sum="R0";	
		if(R1)
			sum="R1";
		if(R2)
			sum="R2";
		if(R3)
			sum="R3";			//only one if statement will be true
		if(R4)
			sum="R4";
		if(R5)
			sum="R5";
		if(R6)
			sum="R6";
		if(R7)
			sum="R7";
		return  sum;
	}
	public static int toDecimal (boolean[] temp){
		int sum=0;
		boolean sign=temp[0];
		boolean[] x=new boolean[temp.length];
		for(int j=0;j<x.length;j++)
			x[j]=temp[j];		//array is copied to new one 
		
		if(sign){							//if value is negative
			for(int i=0;i<x.length;i++)
				x[i]=!x[i];					//to get 2'complement of value 
		}									//make all 0 to 1, all 1 to 0 
		
		int j=x.length-2;
		for(int k=1;k<x.length;k++){
			if(x[k])
				sum+=Math.pow(2,j);			//getting decimal value
			j--;
		}
		
		if(sign){
			sum++;							// final step of getting 2'complement
			sum=-sum;						
		}
		return sum;
	}
	            
	     
		
public static String toHex(boolean[] x) {
	String result = "";
	String binary = "";
	String a = "";
	String b = "";
	String c = "";

    for(int i =0; i < x.length; i++){
    	if(x[i]== false)
    		binary += "0";       //get string representation of boolean input
    	else
    		binary += "1";
    }
   if(x.length % 4 == 1 ){
	  if(binary.charAt(0) == '1' ){
		  binary = "111" + binary; 
	  }else{
		  binary = "000" + binary;       //do the sign extension
	  }
   }
   else if(x.length % 4 == 2 ){
	  if(binary.charAt(0) == '0') {
		  binary = "00" + binary;
	  }else{
	   binary = "11" + binary;         //make the length of offset is divisible by 4
	  }
   }
   else if(x.length % 4 == 3){
	   if(binary.charAt(0) == '0'){
	   binary = "0" + binary;
	   }else{
	   binary = "1" + binary;
       }
   }
   //take 4 by 4 of the offset to get hex type
   
   if(binary.length() == 4 ){
   
   for(int j=0; j<4;j++){
    	a += binary.charAt(j);
    	}
    result = name(a);
    } 
    else if(binary.length() == 8){
    	for(int j=0; j<4;j++){
        	a += binary.charAt(j);
        	}
    	for(int j=4;j<8;j++){
    	b += binary.charAt(j);
    }
    result = name(a) + name(b);
    }
    else if(binary.length() == 12){
    	
    	for(int j=0; j<4;j++){
        	a += binary.charAt(j);
        	}
    	for(int j=4;j<8;j++){
    	b += binary.charAt(j);
    }
    for(int j=8;j<12;j++){
    	c += binary.charAt(j);
    }
    result = name(a)+ name(b) + name(c);
    }
   
return result;
}
//method to use hex table
public static String name(String a){
	String result = "";
    
    if(a.equals("0000")){
    	result = "0";
    }else if(a.equals("0001")){
    	result = "1";
    }else if(a.equals("0010")){
    	result = "2";
    }else if(a.equals("0011")){
    	result = "3";
    }else if(a.equals("0100")){
    	result = "4";
    }else if(a.equals("0101")){
    	result = "5";
    }else if(a.equals("0110")){
    	result = "6";
    }else if(a.equals("0111")){
    	result = "7";
    }else if(a.equals("1000")){
    	result = "8";
    }else if(a.equals("1001")){
    	result = "9";
    }else if(a.equals("1010")){
    	result = "A";
    }else if(a.equals("1011")){
    	result = "B";
    }else if(a.equals("1100")){
    	result = "C";
    }else if(a.equals("1101")){
    	result = "D";
    }else if(a.equals("1110")){
    	result = "E";
    }else if(a.equals("1111")){
    	result = "F";
    }
    
    return result;
    }
}







			