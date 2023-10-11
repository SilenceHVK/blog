package me.hvkcoder.java_basic.algorithm;

import lombok.Getter;
import me.hvkcoder.java_basic.algorithm.visualAlgo.AlgoFrame;
import me.hvkcoder.java_basic.algorithm.visualAlgo.AlgoVisHelper;

import javax.swing.*;
import java.awt.*;


/**
 * 选择排序
 *
 * @author h_vk
 * @since 2023/10/10
 */
public class SelectionSort {
	public static void main(String[] args) {
		int DELAY = 40;
		SortData sortData = new SortData(50, 400);
		int[] data = sortData.getNumbers();

		EventQueue.invokeLater(() -> {
			AlgoFrame frame = new AlgoFrame("选择排序", (g2d, dimension) -> {
				int canvasWidth = (int) dimension.getWidth();
				int canvasHeight = (int) dimension.getHeight();

				int w = canvasWidth / sortData.getSize();
				for (int i = 0; i < sortData.getSize(); i++) {
					if (i < sortData.getOrderedIndex()) {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Green);
					} else {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Grey);
					}

					if (i == sortData.getCurrentMinIndex()) {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Blue);
					}

					if (i == sortData.getCurrentCompareIndex()) {
						AlgoVisHelper.setColor(g2d, AlgoVisHelper.Red);
					}
					AlgoVisHelper.fillRectangle(g2d, i * w + 1, canvasHeight - data[i], w - 1, data[i]);
				}
			});

			frame.animation(DELAY, () -> {
				for (int i = 0; i < sortData.getSize(); i++) {
					int min = i;
					sortData.setDataIndex(i, min, -1, frame, DELAY);
					for (int j = i + 1; j < sortData.getSize(); j++) {
						sortData.setDataIndex(i, min, j, frame, DELAY);
						if (data[min] > data[j]) {
							min = j;
							sortData.setDataIndex(i, min, min, frame, DELAY);
						}
					}
					if (min != i) {
						int tmp = data[min];
						data[min] = data[i];
						data[i] = tmp;
					}
				}
				sortData.setDataIndex(sortData.getSize(), -1, -1, frame, DELAY);
				Thread.currentThread().interrupt();
				System.out.println("排序完成");
			});
		});
	}

	@Getter
	private static class SortData {
		private int[] numbers;
		private final int size;
		private int orderedIndex; // 已排好序的索引区间
		private int currentMinIndex; // 当前最小元素的索引
		private int currentCompareIndex; // 当前比较元素的索引

		public SortData(int size, int maxValue) {
			this.size = size;
			this.generatorData(size, maxValue);
			this.orderedIndex = -1;
			this.currentMinIndex = -1;
			this.currentCompareIndex = -1;
		}

		private void generatorData(int size, int maxValue) {
			this.numbers = new int[size];
			for (int i = 0; i < size; i++) {
				numbers[i] = (int) (Math.random() * maxValue) + 1;
			}
		}

		public void setDataIndex(int orderedIndex, int currentMinIndex, int currentCompareIndex, JFrame frame, int delay) {
			try {
				this.orderedIndex = orderedIndex;
				this.currentMinIndex = currentMinIndex;
				this.currentCompareIndex = currentCompareIndex;
				frame.repaint();
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
