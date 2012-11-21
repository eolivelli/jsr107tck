/**
 *  Copyright 2011 Terracotta, Inc.
 *  Copyright 2011 Oracle, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jsr107.tck;

import org.jsr107.tck.util.ExcludeListExcluder;
import org.jsr107.tck.util.TCKCacheConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.CachingShutdownException;
import javax.cache.transaction.IsolationLevel;
import javax.cache.transaction.Mode;
import javax.transaction.UserTransaction;

import junit.framework.Assert;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for CacheManagers that do not support transactions
 * <p/>
 *
 * @author Yannis Cosmadopoulos
 * @author Greg Luck
 * @since 1.0
 */
public class CacheManagerNoTransactionsTest extends TestSupport {


    @Before
    public void startUp() {
        try {
            Caching.close();
        }   catch (CachingShutdownException e) {
            //this will happen if we call close twice in a row.
        }
    }

    /**
     * Rule used to exclude tests
     */
    @Rule
    public ExcludeListExcluder rule = new ExcludeListExcluder(this.getClass());


    @Test
    public void transactionalStatusWhenNoUserTransaction() throws Exception {
        CacheManager cacheManager = getCacheManager();
        try {
        UserTransaction transaction = cacheManager.getUserTransaction();
        } catch (UnsupportedOperationException e) {
            //expected
        }
    }

    /**
     * The isolation level returned by a non-transactional cache
     */
    @Test
    public void isolationLevelForNonTransactionalCache() throws Exception {
        CacheManager cacheManager = getCacheManager();
        Cache<?, ?> cache = cacheManager.configureCache("test", new TCKCacheConfiguration());
        assertEquals(IsolationLevel.NONE , cache.getConfiguration().getTransactionIsolationLevel());
    }

    /**
     * The transaction mode returned by a non-transactional cache
     */
    @Test
    public void modeForNonTransactionalCache() throws Exception {
        CacheManager cacheManager = getCacheManager();
        Cache<?, ?> cache = cacheManager.configureCache("test", new TCKCacheConfiguration());
        assertEquals(Mode.NONE , cache.getConfiguration().getTransactionMode());
    }

    /**
     * Test various illegal combinations
     */
    @Test
    public void setIncorrectIsolationLevelForTransactionalCache() throws Exception {
        CacheManager cacheManager = getCacheManager();
        
        try {
            cacheManager.configureCache("test", new TCKCacheConfiguration().setTransactions(IsolationLevel.READ_COMMITTED, Mode.NONE));
            Assert.fail("read_committed must have a valid mode");
        } catch (IllegalArgumentException e) {
            //expected
        }
        try {
            cacheManager.configureCache("test", new TCKCacheConfiguration().setTransactions(IsolationLevel.NONE, Mode.LOCAL));
            Assert.fail("failed to provide an isolation level");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }
}