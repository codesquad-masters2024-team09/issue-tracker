package com.issuetracker.domain.milestone.argumentresolver;

import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

public class MilestoneIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MilestoneId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String pathVariableName = Objects.requireNonNull(parameter.getParameterAnnotation(MilestoneId.class)).value();

        if (!pathVariableName.isEmpty()) {
            pathVariableName = parameter.getParameterName();
        }

        assert pathVariableName != null;
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
