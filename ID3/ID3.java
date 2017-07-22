import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ID3 {

	static ArrayList<ArrayList<Object>> result;
	static ArrayList<Object> sam;
	static ArrayList<Object> split;
	static ArrayList<ArrayList<Object>> splitOn;
	static Scanner sc;
	static String name;
	static int count;
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		while(true){
			System.out.println();
			System.out.println("Enter 1- Create Partitions using ID3 and 2-Exit");
			sc = new Scanner(System.in);
			int x = sc.nextInt();
			switch(x){
			case 1: try {
				result = new ArrayList<ArrayList<Object>>();
				sam = new ArrayList<Object>();
				split = new ArrayList<Object>();
				splitOn = new ArrayList<ArrayList<Object>>();
				call();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			case 2: System.out.println("Thank you!!!");System.exit(0);
			break;
			}
		}

	}
	static void call() throws Exception{
		sc = new Scanner(System.in);
		System.out.println("Enter names of the files dataset input-partition output-partition:");
		String dataset =sc.next();
		String partition =sc.next();
		String output = sc.next();
		String mn=null;

		FileReader fileReader = new FileReader(dataset);

		BufferedReader bufferedReader = new BufferedReader(fileReader);

		FileReader fileReader2 = new FileReader(partition);

		BufferedReader br = new BufferedReader(fileReader2);

		mn=bufferedReader.readLine();

		String num[] = mn.split("[ \t]");
		int m= Integer.parseInt(num[0]);
		int n= Integer.parseInt(num[1]);
//		System.out.println("instances: "+m + " features: "+n);
		ArrayList<String> array=new ArrayList<String>();
		for(int i=0;i<m;i++){
			array.add(bufferedReader.readLine());
		}

		ArrayList<ArrayList<Integer>> examples = new ArrayList<ArrayList<Integer>>();  

		// adding input dataset to arraylist
		for(int i=0;i<m;i++){
			String[] e=new String[n];
			e =array.get(i).split("[ \t]");
			ArrayList<Integer> ex = new ArrayList<Integer>(); 
			ex.add(i+1);
			for(int j=0;j<n;j++){
				ex.add(Integer.parseInt(e[j]));
			}
			examples.add(ex);
		}

		//		// Display of input dataset
		//		for(int i=0;i<m;i++){
		//			ArrayList<Integer> ex = examples.get(i); 
		//			for(int j=0;j<n+1;j++){
		//				System.out.print(ex.get(j)+" ");
		//			}
		//
		//			System.out.println();
		//		}

		// adding partitions to arraylist
		String line;
		ArrayList<ArrayList<Object>> partitions = new ArrayList<ArrayList<Object>>();
		while((line=br.readLine())!=null){
			String ex[] = line.split("[ \t]");
			ArrayList<Object> par = new ArrayList<Object>();
			for(int i=0;i<ex.length;i++){
				par.add(ex[i]);
			}
			partitions.add(par);
		}
//		System.out.println("Partitions text: ");
		// Display of input partitions read
/*		for(int i=0;i<partitions.size();i++){
			ArrayList<Object> pr = partitions.get(i); 
			for(int j=0;j<pr.size();j++){
				System.out.print(pr.get(j)+" ");
			}

			System.out.println();
		}		*/

		for(int i=0;i<partitions.size();i++){
			ArrayList<Object> pr = partitions.get(i); 
			partition(pr,examples);
		}
		//		System.out.println("ID3 implementation: ");
		//display(examples);
		//id3(examples);
		// Result display
		//		System.out.println("Result is: ");

		// ******** creating a file output ********* //
		File file = new File(output);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		// ********* write in file ************* //
		for(int i=0;i<result.size();i++){
			ArrayList<Object> pr = result.get(i); 
			for(int j=0;j<pr.size();j++){
				bw.write(pr.get(j).toString());
				//				System.out.print(pr.get(j)+" ");
				if(j!=pr.size()-1)
					bw.write(" ");
			}
			bw.newLine();
			//			System.out.println();
		}

		//Splitting Feature display
		//		System.out.println("Splitting is done on: ");
		System.out.println();
		for(int i=0;i<splitOn.size();i++){
			ArrayList<Object> pr = splitOn.get(i);
			if(pr.size()!=1){
				System.out.print("Partition "+pr.get(0)+" was replaced with partitions ");
				for(int j=2;j<pr.size();j++){
					System.out.print(pr.get(j));
					if(j!=pr.size()-1)
						System.out.print(",");
				}
				System.out.print(" using Feature "+ pr.get(1));
				System.out.println();	
			}
		}

		bufferedReader.close();
		bw.close();
	}
	private static void partition(ArrayList<Object> partition,ArrayList<ArrayList<Integer>> s) {
		// Converting partitions read into arraylist of samples and passing them to ID3 method.
		String pr;
		String names = (String) partition.get(0);
		name = names;
		split = new ArrayList<Object>();
		split.add(names);
		count=0;
//		System.out.println("name is:  "+ names);
		ArrayList<ArrayList<Integer>> samples = new ArrayList<ArrayList<Integer>>();
		sam.add(names);
		for(int j=1;j<partition.size();j++){
			pr = (String) partition.get(j);
			//			System.out.println("pr is: "+ pr);
			for(int k=0;k<s.size();k++){
				//				System.out.println((Integer.parseInt(pr)== s.get(k).get(0)));
				if(Integer.parseInt(pr)== s.get(k).get(0)){
					samples.add(s.get(k));
				}
			}
		}
		id3(samples);
		//System.out.println();
	}

	static void id3(ArrayList<ArrayList<Integer>> s){
		//		ArrayList<Integer> rs = new ArrayList<Integer>();
		if(s.isEmpty()){
//			System.out.println("s is empty");
			return;
		}
		else{
			int n= s.get(0).size();
			int m=s.size();
			ArrayList<Integer> target = new ArrayList<Integer>();
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			for(int i=0;i<m;i++){
				int key =s.get(i).get(n-1);
				if(map.containsKey(key)){
					int value = map.get(key);
					map.put(key, ++value);
				}
				else{
					map.put(key, 1);
				}
			}
			if(map.size()==1){
				int num = sam.size();
				/*System.out.println("sam is: ");
				for(int j=0;j<sam.size();j++){
					System.out.println(sam.get(j));
				}
				System.out.println("num is: "+num);*/
				if(num ==1){
					for(int i=0;i<s.size();i++){
						sam.add(s.get(i).get(0));
					}
					result.add(sam);
				}
				/*if(num !=0){
					for(int i=0;i<s.size();i++){
						sam.add(s.get(i).get(0));
					}
					result.add(sam);
				}*/
				sam=new ArrayList<Object>();
				return;
			}
			else{
				if(!sam.isEmpty()){

					String name1 = "";
					String name2 = "";
					String name3 = "";
					if(split.size() == 4){
						split = new ArrayList<Object>();
						split.add(name);
					}
					if(split.size()==5){
						split = new ArrayList<Object>();
						split.add(name);
					}
					double[] fen = id3Calculation(s,1);
					int f = (int) fen[1];
//					System.out.println();
//					System.out.println(sam.get(0));
//					System.out.println("Feature splitting by: "+(f));
					split.add(f);
//					System.out.println("split as");
					ArrayList<ArrayList<Integer>> s0 = new ArrayList<ArrayList<Integer>>();
					ArrayList<ArrayList<Integer>> s1 = new ArrayList<ArrayList<Integer>>();
					ArrayList<ArrayList<Integer>> s2 = new ArrayList<ArrayList<Integer>>();
					for(int i=0;i<m;i++){
						int feature = s.get(i).get(f);
						if(feature==0){
							s0.add(s.get(i));
						}
						if(feature==1){
							s1.add(s.get(i));
						}
						if(feature==2){
							s2.add(s.get(i));
						}
					}
					if(!s0.isEmpty()){
						display(s0);
					}
					if(!s1.isEmpty()){
//						System.out.println("Inside empty display: "+sam.get(0));
						display(s1);
					}
					if(!s2.isEmpty())
						display(s2);
					//result.add(rs);
					splitOn.add(split);

					if(split.size() == 4){
						name1=(String) split.get(2);
						name2= (String) split.get(3);
					}
					if(split.size()==5){
						name1=(String) split.get(2);
						name2= (String) split.get(3);
						name3 = (String) split.get(4);
					}
					name = name1;
					count=0;
					id3(s0);
					name=name2;
					count=0;
					id3(s1);
					count=0;
					name=name3;
					id3(s2);
//					System.out.println();
				}
			}
		}
	}

	static void display(ArrayList<ArrayList<Integer>> num){
		//ArrayList<Integer> result = new ArrayList<Integer>();
//		String nam =(String) sam.get(0);
		String nam=name;
//		System.out.println("nam is: "+nam);
		int n= count;
		String names = nam+n;
		sam=new ArrayList<Object>();
		sam.add(names);
		split.add(names);
		for(int j=0;j<num.size();j++){
			sam.add(num.get(j).get(0));
			/*System.out.print(num.get(j).get(0));
			if(j!=num.size()-1){
				System.out.print(",");
			}*/
		}
		/*System.out.println();
		System.out.println("In display sam is: ");
		for(int j=0;j<sam.size();j++){
			System.out.print(" "+sam.get(j));
		}
		System.out.println("  ");*/
		result.add(sam);
		count++;
	}

	static double[] id3Calculation(ArrayList<ArrayList<Integer>> s, int feature){
		int n=s.get(0).size();
		if(feature>=n-1){
			double[] fen = new double[2];
			fen[0]=1.0;
			fen[1]=n-1;
			return fen;
		}
		else{
			ArrayList<Integer> arr = new ArrayList<Integer>();
			for(int i=0;i<s.size();i++){
				arr.add(s.get(i).get(feature));
			}
			ArrayList<Integer> target = new ArrayList<Integer>();
			for(int i=0;i<s.size();i++){
				target.add(s.get(i).get(n-1));
			}
			double[] prob = probability(arr);
			double[] en = conditionalEntropy(arr,target);
			double e0 = entropy(en[0],(1.0-en[0]));
			double e1 = entropy(en[1],(1.0-en[1]));
			double e2 = entropy(en[2],(1.0-en[2]));
			//			System.out.println("e0: "+e0+" en[0]: "+en[0]);
			//			System.out.println("e1: "+e1+" en[1]: "+en[1]);
			//			System.out.println("e2: "+e2+" en[2]: "+en[2]);
			double totalEntropy1 = prob[0]*e0+prob[1]*e1+prob[2]*e2;
			double[] featureEntropy1 = new double[2];
			featureEntropy1[0]=totalEntropy1;
			featureEntropy1[1]=feature;
			double[] featureEntropy2 = new double[2];
			featureEntropy2 = id3Calculation(s,++feature);
			double totalEntropy2 =featureEntropy2[0];
			//			System.out.println("In feature entropy calculations: ");
			// comparing entropies and selecting the one with least entropy;
			// selecting the feature which gives least entropy
			//			System.out.println("TE1: "+ totalEntropy1+ " TE2: "+totalEntropy2 );
			if(totalEntropy1<totalEntropy2){
				//				System.out.println("in if");
				//				System.out.println();
				return featureEntropy1;
			}
			else{
				//				System.out.println("in else");
				//				System.out.println();
				return featureEntropy2;
			}
		}
	}

	// conditional entropy of target attribute w.r.t feature calculation
	static double[] conditionalEntropy(ArrayList<Integer> arr, ArrayList<Integer> target){
		double count_zero=0.0;
		double count_one=0.0;
		double count_two=0.0;
		int one=0;
		int zero=0;
		int two=0;
		for(int i=0;i<arr.size();i++){
			if(arr.get(i)==0){
				if(target.get(i)==0){
					count_zero++;
				}
				zero++;
			}
			if(arr.get(i)==1){
				if(target.get(i)==0){
					count_one++;
				}
				one++;
			}
			if(arr.get(i)==2){
				if(target.get(i)==0){
					count_two++;
				}
				two++;
			}
		}
		double[] result = new double[3];
		if(zero==0)
			zero=1;
		if(one==0)
			one=1;
		if(two==0)
			two=1;
		result[0] = count_zero/zero;
		result[1] = count_one/one;
		result[2] = count_two/two;
		return result;
	}

	//probability calculation
	static double[] probability(ArrayList<Integer> arr){
		double count_zero=0.0;
		double count_one=0.0;
		double count_two=0.0;
		int n = arr.size();

		for(int i=0;i<arr.size();i++){
			if(arr.get(i)==0)
				count_zero++;
			if(arr.get(i)==1)
				count_one++;
		}

		if((int) (count_zero+count_one) != arr.size())
			count_two =arr.size() - (count_zero+count_one);

		//		System.out.println("Probability of 0 is: "+(count_zero/n));
		//		System.out.println("Probability of 1 is: "+((count_one/n)));
		//		System.out.println("Probability of 2 is: "+((count_two/n)));

		double[] prob = new double[3];
		prob[0]=(count_zero/n);
		prob[1]=(count_one/n);
		prob[2]=(count_two/n);
		return prob;
	}

	//entropy calculation
	static double entropy(double a, double b){
		if(a==0)
			a=1;
		if(b==0)
			b=1;
		double en = (a*Math.log(1/a)+b*Math.log(1/b))/Math.log(2);
		//		System.out.println("entropy is: "+en);
		return en;
	}
}
