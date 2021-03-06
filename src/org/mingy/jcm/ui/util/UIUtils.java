package org.mingy.jcm.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.mingy.kernel.bean.LabelValueBean;
import org.mingy.kernel.util.Langs;
import org.mingy.kernel.util.Validators;

public abstract class UIUtils {

	public static void initCombo(ComboViewer combo, String prefix,
			Class<?> valueType) {
		combo.setContentProvider(new ArrayContentProvider());
		final Map<Object, String> map = new HashMap<Object, String>();
		final List<Object> values = new ArrayList<Object>();
		for (LabelValueBean bean : Langs.enumLabelValues(prefix)) {
			Object value = convert(bean.getValue(), valueType);
			map.put(value, bean.getLabel());
			values.add(value);
		}
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return map.get(element);
			}
		});
		combo.setInput(values.toArray());
	}

	private static Object convert(String value, Class<?> valueType) {
		if (valueType == String.class) {
			return value;
		} else if (valueType == Boolean.class || valueType == boolean.class) {
			return Boolean.parseBoolean(value);
		} else if (valueType == Byte.class || valueType == byte.class) {
			return Byte.parseByte(value);
		} else if (valueType == Short.class || valueType == short.class) {
			return Short.parseShort(value);
		} else if (valueType == Integer.class || valueType == int.class) {
			return Integer.parseInt(value);
		} else if (valueType == Long.class || valueType == long.class) {
			return Long.parseLong(value);
		} else {
			throw new IllegalArgumentException("valueType error");
		}
	}

	public static ControlDecoration createControlDecoration(Control control,
			Map<Control, ControlDecoration> decoratorMap) {
		ControlDecoration controlDecoration = new ControlDecoration(control,
				SWT.LEFT | SWT.TOP);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		controlDecoration.hide();
		if (decoratorMap != null) {
			decoratorMap.put(control, controlDecoration);
		}
		return controlDecoration;
	}

	public static Binding bindText(DataBindingContext dataBindingContext,
			Control control, Object bean, String propName,
			Map<Control, ControlDecoration> decoratorMap) {
		IObservableValue observeWidget = WidgetProperties.text(SWT.Modify)
				.observe(control);
		return bind(dataBindingContext, observeWidget, control, bean, propName,
				null, null, decoratorMap);
	}

	public static Binding bindText(DataBindingContext dataBindingContext,
			Control control, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap) {
		IObservableValue observeWidget = WidgetProperties.text(SWT.Modify)
				.observe(control);
		return bind(dataBindingContext, observeWidget, control, bean, propName,
				targetToModelConverter, modelToTargetConverter, decoratorMap);
	}

	public static Binding bindText(DataBindingContext dataBindingContext,
			Control control, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap,
			IAggregateValidateListener listener) {
		IObservableValue observeWidget = WidgetProperties.text(SWT.Modify)
				.observe(control);
		return bind(dataBindingContext, observeWidget, control, bean, propName,
				targetToModelConverter, modelToTargetConverter, decoratorMap,
				listener);
	}

	public static Binding bindSelection(DataBindingContext dataBindingContext,
			Control control, Object bean, String propName,
			Map<Control, ControlDecoration> decoratorMap) {
		IObservableValue observeWidget = WidgetProperties.selection().observe(
				control);
		return bind(dataBindingContext, observeWidget, control, bean, propName,
				null, null, decoratorMap);
	}

	public static Binding bindSelection(DataBindingContext dataBindingContext,
			Control control, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap) {
		IObservableValue observeWidget = WidgetProperties.selection().observe(
				control);
		return bind(dataBindingContext, observeWidget, control, bean, propName,
				targetToModelConverter, modelToTargetConverter, decoratorMap);
	}

	public static Binding bindSelection(DataBindingContext dataBindingContext,
			Control control, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap,
			IAggregateValidateListener listener) {
		IObservableValue observeWidget = WidgetProperties.selection().observe(
				control);
		return bind(dataBindingContext, observeWidget, control, bean, propName,
				targetToModelConverter, modelToTargetConverter, decoratorMap,
				listener);
	}

	public static Binding bindSelection(DataBindingContext dataBindingContext,
			Viewer viewer, Object bean, String propName,
			Map<Control, ControlDecoration> decoratorMap) {
		IObservableValue observeWidget = ViewerProperties.singleSelection()
				.observe(viewer);
		return bind(dataBindingContext, observeWidget, viewer.getControl(),
				bean, propName, null, null, decoratorMap);
	}

	public static Binding bindSelection(DataBindingContext dataBindingContext,
			Viewer viewer, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap) {
		IObservableValue observeWidget = ViewerProperties.singleSelection()
				.observe(viewer);
		return bind(dataBindingContext, observeWidget, viewer.getControl(),
				bean, propName, targetToModelConverter, modelToTargetConverter,
				decoratorMap);
	}

	public static Binding bindSelection(DataBindingContext dataBindingContext,
			Viewer viewer, Object bean, String propName,
			Converter targetToModelConverter, Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap,
			IAggregateValidateListener listener) {
		IObservableValue observeWidget = ViewerProperties.singleSelection()
				.observe(viewer);
		return bind(dataBindingContext, observeWidget, viewer.getControl(),
				bean, propName, targetToModelConverter, modelToTargetConverter,
				decoratorMap, listener);
	}

	private static Binding bind(DataBindingContext dataBindingContext,
			IObservableValue observeWidget, Control control, Object bean,
			String propName, Converter targetToModelConverter,
			Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap) {
		final ControlDecoration controlDecoration = createControlDecoration(
				control, decoratorMap);
		IObservableValue observeValue = PojoProperties.value(propName).observe(
				bean);
		UpdateValueStrategy targetToModel = new UpdateValueStrategy();
		targetToModel.setConverter(targetToModelConverter);
		targetToModel.setAfterConvertValidator(new Jsr303BeanValidator(bean
				.getClass(), propName, controlDecoration));
		UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
		modelToTarget.setConverter(modelToTargetConverter);
		Binding binding = dataBindingContext.bindValue(observeWidget,
				observeValue, targetToModel, modelToTarget);
		binding.getValidationStatus().addDisposeListener(
				new IDisposeListener() {
					@Override
					public void handleDispose(DisposeEvent event) {
						controlDecoration.dispose();
					}
				});
		return binding;
	}

	private static Binding bind(DataBindingContext dataBindingContext,
			IObservableValue observeWidget, Control control, Object bean,
			String propName, Converter targetToModelConverter,
			Converter modelToTargetConverter,
			Map<Control, ControlDecoration> decoratorMap,
			IAggregateValidateListener listener) {
		final ControlDecoration controlDecoration = createControlDecoration(
				control, decoratorMap);
		IObservableValue observeValue = PojoProperties.value(propName).observe(
				bean);
		UpdateValueStrategy targetToModel = new UpdateValueStrategy();
		targetToModel.setConverter(targetToModelConverter);
		targetToModel.setAfterConvertValidator(new MyJsr303BeanValidator(bean
				.getClass(), propName, controlDecoration, decoratorMap,
				listener));
		UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
		modelToTarget.setConverter(modelToTargetConverter);
		Binding binding = dataBindingContext.bindValue(observeWidget,
				observeValue, targetToModel, modelToTarget);
		binding.getValidationStatus().addDisposeListener(
				new IDisposeListener() {
					@Override
					public void handleDispose(DisposeEvent event) {
						controlDecoration.dispose();
					}
				});
		return binding;
	}

	private static class MyJsr303BeanValidator extends Jsr303BeanValidator {

		private Map<Control, ControlDecoration> decoratorMap;
		private IAggregateValidateListener listener;

		public MyJsr303BeanValidator(Class<?> clazz, String propName,
				ControlDecoration controlDecoration,
				Map<Control, ControlDecoration> decoratorMap,
				IAggregateValidateListener listener) {
			super(clazz, propName, controlDecoration);
			this.decoratorMap = decoratorMap;
			this.listener = listener;
		}

		@Override
		public IStatus validate(Object value) {
			IStatus status = super.validate(value);
			if (status.isOK()) {
				listener.onValidateFinish(!UIUtils.hasError(decoratorMap));
			} else {
				listener.onValidateFinish(false);
			}
			return status;
		}
	}

	public static boolean hasError(Control control,
			Map<Control, ControlDecoration> decoratorMap) {
		ControlDecoration controlDecoration = decoratorMap.get(control);
		return controlDecoration != null
				&& controlDecoration.getDescriptionText() != null;
	}

	public static boolean hasError(Map<Control, ControlDecoration> decoratorMap) {
		for (ControlDecoration controlDecoration : decoratorMap.values()) {
			if (controlDecoration.getDescriptionText() != null) {
				return true;
			}
		}
		return false;
	}

	public static String getErrorMessage(
			Map<Control, ControlDecoration> decoratorMap) {
		StringBuilder sb = new StringBuilder();
		for (ControlDecoration controlDecoration : decoratorMap.values()) {
			if (controlDecoration.getDescriptionText() != null) {
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(controlDecoration.getDescriptionText());
			}
		}
		return sb.length() > 0 ? sb.toString() : null;
	}

	public static <T> String getErrorMessage(
			Set<ConstraintViolation<T>> violations) {
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<T> violation : violations) {
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(violation.getMessage());
			}
			return sb.toString();
		}
		return null;
	}

	public static String validate(Object bean) {
		Set<ConstraintViolation<Object>> violations = Validators.validate(bean);
		return getErrorMessage(violations);
	}
}
