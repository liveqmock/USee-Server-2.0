package com.usee.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.usee.dao.DanmuDao;
import com.usee.model.Danmu;
import com.usee.model.UserTopic;
@Service
public class DanmuDaoImp implements DanmuDao {
	@Resource
	private SessionFactory sessionFactory;

	/**
	 * 把弹幕数据保存到数据库
	 */
	public void saveDanmu(Danmu danmu) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.save(danmu);
		session.flush();
	}

	/**
	 * 根据id获取弹幕
	 */
	public Danmu getDanmu(int id) {
		String hql = "from Danmu d where d.id =?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, id);
		return (Danmu)query.uniqueResult();  
	}
	
	/**
	 * 根据topicId获取弹幕列表
	 */
	public List<Danmu> getDanmuList(String topicId) {
		// TODO Auto-generated method stub
		String hql = "from Danmu d where d.topicId = ? order by d.create_time desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicId);
		return query.list();
	}

	/**
	 * 根据topicId获取弹幕列表并进行分页
	 */
	public List<Danmu> getDanmuList(String topicId, int pageNum, int pageSize) {
		String hql = "from Danmu d where d.topicId = ? order by d.create_time desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, topicId);
		query.setFirstResult((pageNum - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}

    /**
     *根据topicId获取一段时间之内的弹幕列表
     */
    @Override
    public List<Danmu> getLatestDanmuList(String topicId, String startTime, String endTime) {
        String hql = "from Danmu d where d.topicId = ? and d.create_time between ? and ? order by d.create_time desc";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0, topicId);
        query.setString(1, startTime);
        query.setString(2, endTime);
        return query.list();
    }

    /**
	 * ******废弃*******
	 * 根据danmuId获取弹幕详情
	 */
	/*
	public List<Object[]> getDanmuDetails(int danmuId){
		//String hql = "from Danmu as d, User as u where d.userId = u.userId and d.id = ?";
		String sql = "SELECT d.id AS danmuid, devid, d.userID AS userid, status, topicID, lon, lat, " +
                "praisenum, downnum, commentnum, hitnum, d.create_time AS dcreatetime, address, delete_time, " +
                "head, messages, d.userIcon AS duserIcon, gender, nickname, cellphone, password, c.id AS commontid," +
                " sender, receiver, content, c.reply_commentID AS replycommontid, type, c.create_time AS ccreatetime FROM " +
                "danmu d LEFT JOIN user u ON d.userID = u.userID LEFT JOIN comment c ON d.id = c.danmuID WHERE d.id = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP);  
		query.setInteger(0, danmuId);
		List<Object[]> lobj = query.list();
		return lobj;
	}
	*/
	
	/**
	 * 获取最新插入的一条弹幕的Id
	 */
	public int getLatestDanmuId(){
		String sql = "SELECT MAX(id) FROM danmu";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		return (Integer) query.uniqueResult();
	}
	
	/**
	 * 根据danmuId获取topicId
	 */
	public String getTopicIdbyDanmuId(int danmuId){
		String hql = "select topicId from Danmu d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, danmuId);
		return query.uniqueResult().toString();
	}
	
	/**
	 * 根据danmuId获取userID
	 */
	public String getUserIDbyDanmuId(int danmuId){
		String hql = "select d.userId from Danmu d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, danmuId);
		return query.uniqueResult().toString();
	}
		
	/**
	 * 根据UserId获取弹幕
	 */
	public List getDanmubyUserId(String userId){
		String hql = "select d.topicId AS topicid from Danmu d where d.userId =?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		return query.list();
	}

	public boolean updateUserUpDanmu(Boolean isUp, String userId, int danmuId, String upTime) {
		String sql = "insert into userupdanmu values(NULL, :userId, :danmuId, :upTime)";
		String sql2 = "delete from userupdanmu where userID = :userId and danmuID = :danmuId";
		Query query = null;
		if(isUp){
			query = sessionFactory.getCurrentSession().createSQLQuery(sql2);
		}
		else {
			query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		}
		query.setParameter("userId", userId);
		query.setParameter("danmuId", danmuId);
		query.setParameter("upTime", upTime);
		if(query.executeUpdate() != 0)
		{
			return true;
		}
		else{
			return false;
		}
	}

	public boolean updateUserDownDanmu(Boolean isDown, String userId, int danmuId,
			String downTime) {
		String sql = "insert into userdowndanmu values(NULL, :userId, :danmuId, :downTime)";
		String sql2 = "delete from userdowndanmu where userID = :userId and danmuID = :danmuId";
		Query query = null;
		if(isDown){
			query = sessionFactory.getCurrentSession().createSQLQuery(sql2);
		}
		else {
			query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		}
		query.setParameter("userId", userId);
		query.setParameter("danmuId", danmuId);
		query.setParameter("downTime", downTime);
		if(query.executeUpdate() != 0)
		{
			return true;
		}
		else{
			return false;
		}
	}

	public boolean updateUserFavDanmu(Boolean isFav, String userId,
			int danmuId, String favTime) {
		String sql = "insert into userfavdanmu values(NULL, :userId, :danmuId, :favTime, NULL)";
		String sql2 = "delete from userfavdanmu where userID = :userId and danmuID = :danmuId";
		Query query = null;
		if(isFav){
			query = sessionFactory.getCurrentSession().createSQLQuery(sql2);
		}
		else {
			query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			query.setParameter("favTime", favTime);
		}
		query.setParameter("userId", userId);
		query.setParameter("danmuId", danmuId);
		if(query.executeUpdate() != 0)
		{
			return true;
		}
		else{
			return false;
		}
	}

	public void saveUserDanmu(String userId, int danmuId, String firstVisitTime, String lastVisitTime, int frequency) {
		String sql = "insert into user_danmu values(NULL, :userId, :danmuId, :firstVisitTime, NULL, NULL, :lastVisitTime, :frequency)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("danmuId", danmuId);
		query.setParameter("firstVisitTime", firstVisitTime);
		query.setParameter("lastVisitTime", lastVisitTime);
		query.setParameter("frequency", frequency);
		query.executeUpdate();
		
	}

	public int getUniqueUserDanmubyUserIdandDanmuId(String userId,
			int danmuId) {
		String sql = "select count(*) from user_danmu where userID = ? and danmuID = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, userId);
		query.setInteger(1, danmuId);
		if(query.uniqueResult() != null){
			return 1;
		}
		else {
			return 0;
		}
	}

	public void updateUserDanmu(String userId, int danmuId,
			String lastVisitTime, int frequency) {
		String sql = "update user_danmu set lastVisit_time = :lastVisitTime, frequency = :frequency where userID = :userId and danmuID = :danmuId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("danmuId", danmuId);
		query.setParameter("lastVisitTime", lastVisitTime);
		query.setParameter("frequency", frequency);
		query.executeUpdate();
	}

	public int getLatestFrequency() {
		String sql = "SELECT MAX(frequency) FROM user_danmu";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(query.uniqueResult() == null){
			return 0;
		}
		else{
			return (Integer) query.uniqueResult();
		}
	}

	public List<Object[]> listFavDanmu(String userId) {
		String sql = "select * from userfavdanmu where userID = ?";
		Query query= sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP); 
		query.setParameter(0, userId);
		List<Object[]> lobj = query.list();
		return lobj;
	}
	
	public int saveUserDanmuAction(String userId, int danmuId, int action,
			String actionTime) {
		String sql = "insert into updowndanmu values(NULL, :userId, :danmuId, :action, :actionTime)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("danmuId", danmuId);
		query.setParameter("action", action);
		query.setParameter("actionTime", actionTime);
		return query.executeUpdate();
	}

	public int updateUserDanmuAction(String userId, int danmuId, int action,
			String actionTime) {
		String sql = "update updowndanmu set action = :action, action_time = :actionTime where userID = :userId and danmuID = :danmuId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("danmuId", danmuId);
		query.setParameter("action", action);
		query.setParameter("actionTime", actionTime);
		return query.executeUpdate();
	}

	public int getUniqueUpDownDanmubyUserIdandDanmuId(String userId,
			int danmuId) {
		String sql = "select * from updowndanmu where userID = ? and danmuID = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, userId);
		query.setInteger(1, danmuId);
		if(query.uniqueResult() != null){
			return 1;
		}
		else {
			return 0;
		}
	}

	public int getActionbyUserIdandDanmuId(String userId, int danmuId) {
		String sql = "select action from updowndanmu where userID = ? and danmuID = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, userId);
		query.setInteger(1, danmuId);
		if(query.uniqueResult() != null){
			return Integer.parseInt(query.uniqueResult().toString());
		}
		else {
			return 0;
		}
	}

	public boolean checkUserFavDanmu(String userId, int danmuId) {
		String sql = "select * from userfavdanmu where userID = ? and danmuID = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, userId);
		query.setInteger(1, danmuId);
		if(query.uniqueResult() != null){
			return true;
		}
		else {
			return false;
		}
	}

	public int getLatestDanmuIdByUserIdAndTopicId(String userId, String topicId) {
		String sql = "SELECT MAX(id) FROM danmu where userID = ? and topicID = ?";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setString(0, userId);
		query.setString(1, topicId);
		if(query.uniqueResult() == null){
			return -1;
		}
		else{
			return (Integer) query.uniqueResult();
		}
	}
	
}
