package org.mingy.jcm.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.mingy.jcm.ui.model.Resource;

public class ResourceEditorInput implements IEditorInput {

	private Resource item;

	public ResourceEditorInput(Resource resource) {
		if (resource.getType() == Resource.TYPE_ITEM) {
			item = resource;
		} else {
			item = new Resource(Resource.TYPE_ITEM);
			item.setParent(resource);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ResourceEditorInput ? item
				.equals(((ResourceEditorInput) obj).item) : false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(Resource.class)) {
			return item;
		} else {
			return item.getValue();
		}
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "";
	}
}
