package net.ukr.andy777;

/*
 Lesson06.5
 Реализуйте программу, которая с периодичностью 1 сек, будет проверять состояние заданного каталога. 
 Если в этом каталоге появится новый файл или исчезнет существующий, то выведется сообщение об этом событии. 
 Программа должна работать в параллельном потоке выполнения. 
 */

import java.io.File;

public class Main {
	public static void main(String[] args) {
		File folder = new File(".\\1");
		Thread thr = new Thread (new FileWatcher (folder));
		thr.start();
		
		// зупинка виконання через ХХ000 сек. про всяк випадок.
		try{
			Thread.sleep(60000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		thr.interrupt();
	}
}
