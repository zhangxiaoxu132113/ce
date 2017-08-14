package com.water.crawl.core.category;

public class ClassConditionalProbability {
	private static TrainingDataManager tdm = new TrainingDataManager();
	private static final float M = 0F;

	/**
	 * 计算类条件概率
	 * 
	 * @param x
	 *            给定的文本属性
	 * @param c
	 *            给定的分类
	 * @return 给定条件下的类条件概率
	 */
	public static float calculatePxc(String x, String c) {
		float ret = 0F;
		float Nxc = tdm.getCountContainKeyOfClassification(c, x);
		float Nc = tdm.getTrainingFileCountOfClassification(c);
		float V = tdm.getTraningClassifications().length;
		ret = (Nxc + 1) / (Nc + M + V); // 为了避免出现0这样极端情况，进行加权处理
		return ret;
	}
}
