package com.test.introduction;

public class DefaultUsageTracked implements UsageTracked {

	@Override
	public void incrementUseCount() {
		System.out.println("increment use count has been called...");
	}

}
