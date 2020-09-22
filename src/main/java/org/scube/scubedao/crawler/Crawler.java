package org.scube.scubedao.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.scube.exception.ScubeException;
import org.scube.scubedao.model.DaoProject;
import org.scube.scubedao.model.Schema;

public abstract class Crawler {

	public static Crawler getCrawler(DaoProject project) throws ScubeException {
		Object crawlerInstance = null;
		try {
			Class<? extends Object> crawlerClass = Class.forName(project.getCrawlerConfig()
					.getClassName());
			crawlerInstance = crawlerClass.newInstance();
			if (!(crawlerInstance instanceof Crawler)) {
				throw new ScubeException(
						"crawlerClass "
								+ project.getCrawlerConfig().getClassName()
								+ " has to be subclass of  org.scube.scubedao.crawler.Crawler");
			}
		} catch (ClassNotFoundException e) {
			throw new ScubeException("crawlerClass "
					+ project.getCrawlerConfig().getClassName() + " not found",
					e);
		} catch (InstantiationException e) {
			throw new ScubeException("crawlerClass "
					+ project.getCrawlerConfig().getClassName()
					+ " can not be intiated", e);
		} catch (IllegalAccessException e) {
			throw new ScubeException("crawlerClass "
					+ project.getCrawlerConfig().getClassName()
					+ " can not be accessed", e);
		}

		return (Crawler) crawlerInstance;
	}

	protected Connection getConnection(DaoProject project) throws SQLException,
			ClassNotFoundException {
		Class.forName(project.getCrawlerConfig().getDriverName());
		return DriverManager.getConnection(project.getUrl(), project
				.getUserName(), project.getPassword());
	}

	public abstract Schema getSchema(DaoProject ormProject)
			throws ScubeException;

}