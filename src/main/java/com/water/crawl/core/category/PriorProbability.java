package com.water.crawl.core.category;

public class PriorProbability {
	private static TrainingDataManager tdm =new TrainingDataManager();
    /**
    * 先验概率
    * @param c 给定的分类
    * @return 给定条件下的先验概率
    */
    public static float calculatePc(String c)
    {
        float ret = 0F;
        float Nc = tdm.getTrainingFileCountOfClassification(c);
        float N = tdm.getTrainingFileCount();
        ret = Nc / N;
        return ret;
    }
}
