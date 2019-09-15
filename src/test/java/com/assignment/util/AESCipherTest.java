package com.assignment.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AESCipherTest {

	@Test
	public void encryptAndDecrypt() throws Exception {
		String pwd = "Welcome!";
		String encryptStr = AESCipher.encrypt(pwd);
		String decryptStr = AESCipher.decrypt(encryptStr);
		assertEquals(pwd, decryptStr);
	}

}
