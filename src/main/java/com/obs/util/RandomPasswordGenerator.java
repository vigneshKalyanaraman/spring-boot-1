package com.obs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
@Service
public class RandomPasswordGenerator {
	private static final String CHAR_LIST =
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int RANDOM_STRING_LENGTH = 10;

	private Random random =  new Random();

	/**
	 * This method generates random string
	 * @return
	 */
	public String generateRandomString(){

		StringBuilder randStr = new StringBuilder();
		for(int i=0; i<RANDOM_STRING_LENGTH; i++){
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	/**
	 * This method generates random numbers
	 * @return int
	 */
	private int getRandomNumber() {
		int randomInt = 0;
		randomInt = random.nextInt(CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	public String generatePwd(String email)
	{
		String username = email.split("@")[0];
		String unameSub = username.substring(0, username.length()>4?3:username.length());
		return unameSub+String.valueOf (random.nextInt(999999));
	}

	public String randomNumbers() {
		List<Integer> numbers = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			numbers.add(i);
		}

		Collections.shuffle(numbers);
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < 6; i++){
			sb.append(numbers.get(i).toString());
		}

		return sb.toString();
	}

}
