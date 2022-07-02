package ppPackage;

import java.util.Scanner;

public class Petya {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String first = scanner.nextLine();
		String second = scanner.nextLine();
		
		System.out.println(first.toLowerCase().compareTo(second.toLowerCase()));
			
		
		
		scanner.close();
		
	}

}
