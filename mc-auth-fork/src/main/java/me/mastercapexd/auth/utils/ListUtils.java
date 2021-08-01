package me.mastercapexd.auth.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
	public <T> List<List<T>> chopList(List<T> list, final int L) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += L) {
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}

	public <T> List<T> getListPage(List<T> source, int page, int count) {
		List<T> localpage = new ArrayList<>();
		int to = (page * count - 1);
		int from = (to - count);
		for (int i = to; i > from; i--)
			try {
				localpage.add(source.get(i));
			} catch (Exception e) {
			}

		return localpage;
	}

	public int getMaxPages(int totalItemCount, int onePageLimit) {
		return (int) Math.ceil((float)totalItemCount/onePageLimit);
	}
}
