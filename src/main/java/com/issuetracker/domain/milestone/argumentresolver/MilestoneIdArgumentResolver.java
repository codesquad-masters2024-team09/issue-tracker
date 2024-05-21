package com.issuetracker.domain.milestone.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MilestoneIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMilestoneIdAnnotation = parameter.hasParameterAnnotation(MilestoneId.class);
        boolean hasMilestoneId = String.class.isAssignableFrom(parameter.getParameterType());

        return hasMilestoneIdAnnotation && hasMilestoneId;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String pathVariableName = parameter.getParameterAnnotation(MilestoneId.class).value();

        if (!pathVariableName.isEmpty()) {
            pathVariableName = parameter.getParameterName();
        }

        String value = webRequest.getParameter(pathVariableName);

        if (value == null || value.isBlank() || value.length() > 30) {
            throw new IllegalArgumentException("마일스톤 ID는 비어있거나 30자를 넘을 수 없습니다.");
        }

        return value;
    }
}
