import java.text.NumberFormat;

public class Memory {

	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		sb.append("free memory: " + format.format(freeMemory / 1048576) + "<br/>\n");
		sb.append("allocated memory: " + format.format(allocatedMemory / 1048576) + "<br/>\n");
		sb.append("max memory: " + format.format(maxMemory / 1048576) + "<br/>\n");
		sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1048576) + "<br/>\n");

		System.out.println(sb);

	}

}
