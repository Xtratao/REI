import java.util.ArrayList;

public class Test {

	static void f(ArrayList<String> l, String s)
	{
		l.add(s);
	}
	public static void main(String[] args) {
		
		ArrayList<String> l = new ArrayList<String>();
		l.add("mot1");
		
		f(l,"mot2");
		
		System.out.println(l);
//		File f = new File(".");
//		String[] str = f.list();
//		for (String s : str) {
//			System.out.println(s);
//		}
	}

}
