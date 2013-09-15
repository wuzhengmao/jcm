package org.mingy.jcm.model;

public interface ICachable<T extends ICachable<?>> extends IIdentity {

	void copyTo(T bean);

}
