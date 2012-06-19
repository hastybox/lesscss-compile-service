package com.hastybox.lesscss.compileservice;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachingLessCompileServiceWrapper implements LessCompileService {

	/**
	 * logger
	 */
	private static final Logger LOGGER;
	
	/**
	 * cache key prefix of code
	 */
	private static final String CODE_PREFIX = "code:";
	
	/**
	 * cache key prefix of file
	 */
	private static final String FILE_PREFIX = "file:";

	static {
		LOGGER = LoggerFactory
				.getLogger(CachingLessCompileServiceWrapper.class);
	}

	/**
	 * simple cache
	 */
	protected Map<String, String> cache;

	/**
	 * wrapped compile service;
	 */
	protected LessCompileService compileService;

	/**
	 * @return the compileService
	 */
	protected LessCompileService getCompileService() {
		return compileService;
	}

	/**
	 * constructor
	 * 
	 * @param compileService
	 */
	public CachingLessCompileServiceWrapper(LessCompileService compileService) {
		cache = new ConcurrentHashMap<String, String>();
		this.compileService = compileService;
	}

	public CachingLessCompileServiceWrapper() {
		this(null);
	}

	public String compileCode(File lessFile) {
		String cacheKey = FILE_PREFIX + lessFile.getPath();
		LOGGER.debug("Looking up {} in cache.", lessFile);

		String cssCode = cache.get(cacheKey);

		if (cssCode == null) {
			LOGGER.debug("Did not find {} in cache. Going to compile.",
					lessFile);

			cssCode = getCompileService().compileCode(lessFile);

			cache.put(cacheKey, cssCode);
		}

		return cssCode;
	}

	public String compileCode(String lessCode) {
		String cacheKey = CODE_PREFIX + lessCode;
		LOGGER.debug("Looking up {} in cache.", lessCode);

		String cssCode = cache.get(cacheKey);

		if (cssCode == null) {
			LOGGER.debug("Did not find {} in cache. Going to compile.",
					lessCode);

			cssCode = getCompileService().compileCode(lessCode);

			cache.put(cacheKey, cssCode);
		}

		return cssCode;
	}

	/**
	 * clears all entries from cache.
	 */
	public void invalidateCache() {
		LOGGER.debug("Invalidating complete cache.");

		cache.clear();
	}

	/**
	 * removes entry for {@code path} from cache
	 * 
	 * @param path
	 *            key to remove from cache
	 */
	public void invalidateCodeCache(String path) {
		LOGGER.debug("Invalidating {} key from cache", path);
		cache.remove(CODE_PREFIX + path);
	}
	
	/**
	 * removes entry for {@code path} from cache
	 * 
	 * @param path
	 *            key to remove from cache
	 */
	public void invalidateFileCache(String path) {
		LOGGER.debug("Invalidating {} key from cache", path);
		cache.remove(FILE_PREFIX + path);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("compileService", compileService)
				.append("cache", cache).toString();
	}

}