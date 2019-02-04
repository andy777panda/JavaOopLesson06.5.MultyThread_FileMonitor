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
		boolean isFile;
		int qF = 0; // змінна рахує кількість незмінених файлів/папок

		// цикл проходу по старому переліку вмісту папки
		for (File file : this.fileArray) {
			isFile = false;
			for (File fileNew : fileArrayNew)
				// кожен з нових фалів/папок порівнюється зі старим
				if (file.equals(fileNew)) {
					isFile = true;
					break;
				}
			if (!isFile) // якщо старий файл/папку не знайдено в новому переліку, то його видалили
				sb.append(System.getProperty("line.separator")).append("remove: \t" + file.getName());
			else
				// якщо старий файл/папку знайдено в новому переліку, то лічильник незмінених збільшується
				qF++;
		}

		// цикл проходу по новому переліку вмісту папки
		for (File fileNew : fileArrayNew) {
			isFile = false;
			for (File file : this.fileArray)
				// кожен зі старих фалів/папок порівнюється з новим
				if (fileNew.equals(file)) {
					isFile = true;
					break;
				}
			if (!isFile) // якщо новий файл/папку не знайдено в старому переліку, то його додали
				sb.append(System.getProperty("line.separator")).append(
						(fileNew.isFile() ? "add file: \t" : "add folder: \t") + fileNew.getName() + "\t"
								+ fileNew.length());
			else
				// якщо новий файл/папку знайдено в старому переліку, то лічильник незмінених збільшується
				qF++;
		}

		// якщо кількість нзмінених == кількості елементів старого та нового переліку -- то змін не було
		if (qF == (this.fileArray.length + fileArrayNew.length))
			sb.append(" -- NO changes in folder");

		setFileArray(fileArrayNew);
		return sb.toString();
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
