package com.xuan.bigappleui.lib.fileexplorer.core;

import java.util.Comparator;
import java.util.HashMap;

import com.xuan.bigappleui.lib.fileexplorer.entity.FileInfo;

/**
 * 文件排列帮助工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-10 上午11:00:32 $
 */
public class FileSortHelper {
	/**
	 * 排列方式枚举
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2014-12-10 上午11:00:59 $
	 */
	public enum SortMethod {
		name, size, date, type
	}

	/** 按某种方式排序，默认按时间 */
	private SortMethod useSortMethod = SortMethod.date;
	/** 表示文件排前面还是文件夹排前面，true表示文件排前面false表示文件夹排前面，默认false文件夹排在前面 */
	private boolean isFileFirst = false;

	/** 多种排序方式Map */
	private final HashMap<SortMethod, Comparator<FileInfo>> sortMethod2ComparatorMap = new HashMap<SortMethod, Comparator<FileInfo>>();

	/**
	 * 构造方法
	 */
	public FileSortHelper() {
		sortMethod2ComparatorMap.put(SortMethod.name, cmpName);
		sortMethod2ComparatorMap.put(SortMethod.size, cmpSize);
		sortMethod2ComparatorMap.put(SortMethod.date, cmpDate);
		sortMethod2ComparatorMap.put(SortMethod.type, cmpType);
	}

	public SortMethod getUseSortMethod() {
		return useSortMethod;
	}

	public void setUseSortMethod(SortMethod useSortMethod) {
		this.useSortMethod = useSortMethod;
	}

	public boolean isFileFirst() {
		return isFileFirst;
	}

	public void setFileFirst(boolean isFileFirst) {
		this.isFileFirst = isFileFirst;
	}

	/**
	 * 获取排序比较器
	 * 
	 * @return
	 */
	public Comparator<FileInfo> getComparator() {
		return sortMethod2ComparatorMap.get(useSortMethod);
	}

	/**
	 * 文件比较器的基类
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2014-12-10 上午11:14:08 $
	 */
	private abstract class FileComparator implements Comparator<FileInfo> {
		@Override
		public int compare(FileInfo fileInfo1, FileInfo fileInfo2) {
			if (fileInfo1.isDir == fileInfo2.isDir) {
				// 都是文件或者都是文件夹
				return doCompare(fileInfo1, fileInfo2);
			}

			if (isFileFirst) {
				return (fileInfo1.isDir ? 1 : -1);
			} else {
				return fileInfo1.isDir ? -1 : 1;
			}
		}

		/**
		 * 不同排序算法实现这个排序
		 * 
		 * @param fileInfo1
		 * @param fileInfo2
		 * @return
		 */
		protected abstract int doCompare(FileInfo fileInfo1, FileInfo fileInfo2);
	}

	/**
	 * 根据名称排序
	 */
	private final Comparator<FileInfo> cmpName = new FileComparator() {
		@Override
		public int doCompare(FileInfo object1, FileInfo object2) {
			return object1.fileName.compareToIgnoreCase(object2.fileName);
		}
	};

	/**
	 * 根据大小排序
	 */
	private final Comparator<FileInfo> cmpSize = new FileComparator() {
		@Override
		public int doCompare(FileInfo object1, FileInfo object2) {
			return longToCompareInt(object1.fileSize - object2.fileSize);
		}
	};

	/**
	 * 根据时间排序
	 */
	private final Comparator<FileInfo> cmpDate = new FileComparator() {
		@Override
		public int doCompare(FileInfo object1, FileInfo object2) {
			return longToCompareInt(object2.modifiedDate - object1.modifiedDate);
		}
	};

	/**
	 * 根据类型排序
	 */
	private final Comparator<FileInfo> cmpType = new FileComparator() {
		@Override
		public int doCompare(FileInfo object1, FileInfo object2) {
			int result = Util.getExtFromFilename(object1.fileName)
					.compareToIgnoreCase(
							Util.getExtFromFilename(object2.fileName));
			if (result != 0) {
				return result;
			}

			return Util.getNameFromFilename(object1.fileName)
					.compareToIgnoreCase(
							Util.getNameFromFilename(object2.fileName));
		}
	};

	/** long值比较 */
	private int longToCompareInt(long result) {
		return result > 0 ? 1 : (result < 0 ? -1 : 0);
	}

}
