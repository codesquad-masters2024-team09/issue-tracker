package com.issuetracker.domain.milestone.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class MilestoneIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MilestoneId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String pathVariableName = parameter.getParameterAnnotation(MilestoneId.class).value();

        if (!pathVariableName.isEmpty()) {
            pathVariableName = parameter.getParameterName();
        }

        String value = webRequest.getParameter(pathVariableName);

        validateMilestoneId(value, pathVariableName, parameter);

        return value;
    }

    private void validateMilestoneId(String value, String pathVariableName, MethodParameter parameter) throws MethodArgumentNotValidException {
        if (value == null || value.isBlank()) {
            throwValidationException(parameter, pathVariableName, "마일스톤 ID는 비어있거나 null일 수 없습니다.");
        } else if (value.length() > 30) {
            throwValidationException(parameter, pathVariableName, "마일스톤 ID는 30자 이하여야 합니다.");
        }
    }

    private void throwValidationException(MethodParameter parameter, String pathVariableName, String errorMessage) throws MethodArgumentNotValidException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pathVariableName, parameter.getParameterType().getName());
        bindingResult.addError(new ObjectError(pathVariableName, errorMessage));
        throw new MethodArgumentNotValidException(parameter, bindingResult);
    }
}
