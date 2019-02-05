package net.ukr.andy777;

// сервісний клас для повернення методом двох значень різних типів 
final class MyResult {

	private final StringBuilder sb1;
	private final int int1;

	public MyResult(StringBuilder sb1, int int1) {
		this.sb1 = sb1;
		this.int1 = int1;
	}

	public StringBuilder getSb1() {
		return sb1;
	}

	public int getInt1() {
		return int1;
	}
}
