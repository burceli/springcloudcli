package me.bruceli.redis.service;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    boolean existsKey(String key);

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey);

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey);

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key);

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteKey(String... keys);

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public void deleteKey(Collection<String> keys);

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireKey(String key, long time, TimeUnit timeUnit);

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date);

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(String key, TimeUnit timeUnit);

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public void persistKey(String key);


}
