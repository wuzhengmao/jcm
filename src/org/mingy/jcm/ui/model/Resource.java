package org.mingy.jcm.ui.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.mingy.jcm.model.INamedObject;
import org.mingy.jcm.model.PropertyChangeSupportBean;
import org.mingy.kernel.util.Langs;

public class Resource extends PropertyChangeSupportBean implements
		PropertyChangeListener {

	public static final int TYPE_ROOT = 0;
	public static final int TYPE_TEACHER = 1;
	public static final int TYPE_COURSE = 2;
	public static final int TYPE_STUDENT = 3;
	public static final int TYPE_ITEM = 99;

	private int type;
	private Resource parent;
	private IObservableList children = new WritableList();

	private INamedObject value;

	public Resource(int type) {
		this.type = type;
	}

	public Resource(INamedObject value) {
		this.type = TYPE_ITEM;
		this.value = value;
		this.value.addNameChangeListener(this);
	}

	@Override
	public int hashCode() {
		return type * 31 + (value != null ? value.hashCode() : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Resource)) {
			return false;
		} else if (type != ((Resource) obj).type) {
			return false;
		} else if (value == null && ((Resource) obj).value == null) {
			return type == Resource.TYPE_ITEM ? parent
					.equals(((Resource) obj).parent) : true;
		} else {
			return value != null && value.equals(((Resource) obj).value);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		firePropertyChange("label", evt.getOldValue(), evt.getNewValue());
	}

	public int getType() {
		return type;
	}

	public INamedObject getValue() {
		return value;
	}

	public void setValue(INamedObject value) {
		if (this.value != value) {
			if (this.value != null) {
				this.value.removeNameChangeListener(this);
			}
			String label = getLabel();
			this.value = value;
			if (this.value != null) {
				this.value.addNameChangeListener(this);
			}
			firePropertyChange("label", label, getLabel());
		}
	}

	public String getLabel() {
		if (type != TYPE_ITEM) {
			return Langs.getLabel("resource.type", type);
		} else {
			return value != null ? value.getName() : null;
		}
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		firePropertyChange("parent", this.parent, this.parent = parent);
	}

	@SuppressWarnings("unchecked")
	public List<Resource> getChildren() {
		return children;
	}

	public IObservableList getObservableChildren() {
		return children;
	}
}
