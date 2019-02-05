package net.ukr.andy777;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileWatcher implements Runnable {
	private File mainFolder;
	private File[] fileArray;

	public FileWatcher() {
		super();
	}

	public FileWatcher(File mainFolder) {
		super();
		this.mainFolder = mainFolder;
	}

	public File getMainFolder() {
		return mainFolder;
	}

	public void setFileArray(File[] fileArray) {
		this.fileArray = fileArray;
	}

	// метод отримання вмісту папки
	private String getFolderContents() {
		if (mainFolder == null)
			return "None";
		StringBuilder sb = new StringBuilder();
		sb.append("Folder " + mainFolder.getAbsolutePath()).append(System.getProperty("line.separator"));
		for (File file : this.fileArray)
			sb.append((file.isFile() ? "file: \t" : "folder: \t") + file.getName() + "\t" + file.length()).append(
					System.getProperty("line.separator"));
		return sb.toString();
	}

	// метод відслідковування змін вмісту папки
	private String getFolderChanges() {
		if (mainFolder == null)
			return "None";

		StringBuilder sb = new StringBuilder();
		File[] fileArrayNew = this.mainFolder.listFiles();
		MyResult compare;

		// цикл проходу по старому переліку вмісту папки
		compare = compareFolder("remove", this.fileArray, fileArrayNew, sb, 0);

		// цикл проходу по новому переліку вмісту папки
		compare = compareFolder("add", fileArrayNew, this.fileArray, compare.getSb1(), compare.getInt1());


		// якщо кількість нзмінених == кількості елементів старого та нового переліку -- то змін не було
		if (compare.getInt1() == (this.fileArray.length + fileArrayNew.length))
			sb.append(" -- NO changes in folder");
		else
			sb = compare.getSb1();			

		setFileArray(fileArrayNew);
		return sb.toString();
	}

	// метод порівняння вмісту папок
	private MyResult compareFolder(String oper, File[] fileArray1, File[] fileArray2, StringBuilder sb, int qF) {
		for (File file1 : fileArray1) {
			boolean isFile = false;
			for (File file2 : fileArray2)
				// кожен з нових фалів/папок порівнюється зі старим
				if (file1.equals(file2)) {
					isFile = true;
					break;
				}
			if (!isFile) {
				if (oper.equals("remove"))
					// якщо старий файл/папку не знайдено в новому переліку, то його видалили
					sb.append(System.getProperty("line.separator")).append("remove: \t" + file1.getName());
				if (oper.equals("add"))
					// якщо новий файл/папку не знайдено в старому переліку, то його додали
					sb.append(System.getProperty("line.separator")).append(
							(file1.isFile() ? "add file: \t" : "add folder: \t") + file1.getName() + "\t"
									+ file1.length());
			} else
				// якщо старий/новий файл/папку знайдено в новому/старому переліку, то лічильник незмінених збільшується
				qF++;
		}
		return new MyResult(sb, qF);
	}

	@Override
	public void run() {
		Thread thr = Thread.currentThread();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		setFileArray(this.mainFolder.listFiles());
		System.out.println(getFolderContents());
		for (; !thr.isInterrupted();) {
			System.out.print(sdf.format(System.currentTimeMillis()));
			System.out.println(getFolderChanges());
			System.out.println();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
		System.out.println("FileWatcher is stop");
	}
}
