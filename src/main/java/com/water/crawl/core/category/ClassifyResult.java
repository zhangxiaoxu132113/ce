package com.water.crawl.core.category;

public class ClassifyResult {
	public double probility;// 分类的概率
	public String classification;// 分类

	public ClassifyResult() {
		this.probility = 0;
		this.classification = null;
	}
}
