package com.dev9.presentation.support;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;

import com.disney.dtss.gs.identityclient.helper.ServiceResult;
import com.disney.guestcontroller.app.error.enums.ErrorDef;
import com.disney.guestcontroller.app.error.translator.ErrorTranslator;
import com.disney.guestcontroller.app.exception.GuestControllerExceptionBuilder;
import com.disney.guestcontroller.app.util.ServiceErrorUtil;
import com.disney.guestcontroller.error.enums.ErrorCode;
import com.disney.guestcontroller.model.Data;
import com.disney.guestcontroller.model.DataCallResult;
import com.disney.guestcontroller.model.ErrorMessages;
import com.disney.guestcontroller.model.GCError;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Wraps result from underlying network call. Optionally allows services to parse exception and allow managers to decide
 */
public class CallResult<T> {

    private final T payload;

    /**
     * Status code inferred from incoming service result or explicitly set.
     * Not to be used as outgoing status code
     */
    private int statusCode;


    CallResult(T t) {
        Preconditions.checkArgument(t != null, "Result value must not be null");
        payload = t;
    }

    CallResult(List<GCError> errors) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(errors), "Error List must not be empty");
        this.errors = errors;
        payload = null;
    }

    CallResult(ErrorDef errorDef, ServiceResult<T> serviceResult) {
        Preconditions.checkArgument(errorDef != null, "ErrorDef must not be null");
        Preconditions.checkArgument(serviceResult != null, "Service Result must not be null");

        errors.add(errorDef.toGCErrorFromServiceResult(serviceResult));
        this.serviceResult = serviceResult;
        payload = null;
    }

    public static <I, O extends Data> DataCallResult<O> toDataCallResult(CallResult<I> callResult,
            Function<I, O> convert) {

        DataCallResult<O> output = new DataCallResult<>();
        if(callResult.isOk()) {
            output.setData(convert.apply(callResult.get()));
        }
        else {
            output.addGCErrors(callResult.getErrors());
        }
        return output;
    }

    public static <T> CallResult<T> ok(T value) {
        return new CallResult<>(value);
    }

    /**
     * Ok return with specific status, and not set data. This is used when clients do not return data and service is
     * coded to accept http status other than 200 201, etc.. as successful response. ServiceResult is not populated in
     * such cases.
     */
    public static CallResult<?> voidOkWithStatusCode(int httpStatusCode) {
        CallResult<?> callResult = ok(Void.TYPE);
        callResult.setStatusCode(httpStatusCode);
        return callResult;
    }

    /**
     * generic check for errors and throws GuestControllerException if errors exist
     *
     */
    public CallResult<T> throwGCEifError() {
        if (isError()) {
            new GuestControllerExceptionBuilder()
                    .setErrorMessages(new ErrorMessages(getErrors()))
                    .buildAndThrow();
        }
        return this;
    }

    /**
     * Create CallResult with a GCError object. Use this if you need to establish a custom error.
     */
    public static <T> CallResult<T> errorSingle(GCError error) {
        Preconditions.checkArgument(error != null, "Error must not be null");
        return CallResult.errorList(Lists.newArrayList(error));
    }

    /**
     * Create CallResult with a GCError object and associated serviceResult. More often than not you will want to use
     * {@link #errorSingleWithServiceResult(ErrorDef, ServiceResult)} instead.
     */
    public static <T> CallResult<T> errorSingleWithServiceResult(GCError error, ServiceResult serviceResult) {
        Preconditions.checkArgument(error != null, "Error must not be null");
        return CallResult.errorListWithServiceResult(Lists.newArrayList(error), serviceResult);
    }

    /**
     * Create CallResult with an ErrorDef object and associated serviceResult that can be used to augment the error
     * details.
     */
    public static <T> CallResult<T> errorSingleWithServiceResult(ErrorDef errorDef, ServiceResult serviceResult) {
        return new CallResult<>(errorDef, serviceResult);
    }

    /**
     * Create CallResult with a list of GCError objects.
     */
    public static <T> CallResult<T> errorList(List<GCError> errors) {
        CallResult<T> callResult = new CallResult<>(errors);
        callResult.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        return callResult;
    }

    /**
     * Create CallResult with a list of GCError objects and associated serviceResult.
     */
    public static <T> CallResult<T> errorListWithServiceResult(List<GCError> errors, ServiceResult serviceResult) {
        CallResult<T> callResult = new CallResult<>(errors);
        callResult.setServiceResult(serviceResult);
        return callResult;
    }

    /**
     * Create CallResult with a list of GCError objects produced using a ServiceErrorUtil getServiceErrors call, and
     * associated serviceResult. This approach uses an ErrorTranslator for custom mappings and leverages its
     * pre-defined fallback ErrorDef.
     */
    public static <T> CallResult<T> errorsTranslated(ErrorTranslator errorTranslator, ServiceResult serviceResult) {
        return CallResult.errorListWithServiceResult(ServiceErrorUtil.getServiceErrors(serviceResult, errorTranslator),
                serviceResult);
    }

    /**
     * Create CallResult with a list of GCError objects produced using a ServiceErrorUtil getServiceErrors call, and
     * associated serviceResult. This approach uses an ErrorTranslator for custom mappings and leverages its
     * pre-defined fallback ErrorDef.
     */
    public static <T> CallResult<T> errorsTranslated(ErrorTranslator errorTranslator, ServiceResult serviceResult,
            Object... fieldNameParameters) {
        return CallResult.errorListWithServiceResult(ServiceErrorUtil.getServiceErrors(serviceResult, errorTranslator, fieldNameParameters),
                serviceResult);
    }

    /**
     * Create CallResult with a list of GCError objects produced using a ServiceErrorUtil getServiceErrors call, and
     * associated serviceResult. This approach uses an ErrorTranslator for custom mappings and a different fallback
     * ErrorDef than pre-defined in the ErrorTranslator, allowing them to be mixed and matched.
     */
    public static <T> CallResult<T> errorsTranslated(ErrorTranslator errorTranslator, ErrorDef fallbackServiceErrorDef,
            ServiceResult serviceResult) {
        return CallResult.errorListWithServiceResult(
                ServiceErrorUtil.getServiceErrors(serviceResult, errorTranslator, fallbackServiceErrorDef),
                serviceResult);
    }

    /**
     * Create CallResult with a list of GCError objects produced using a ServiceErrorUtil getServiceErrors call, and
     * associated serviceResult. This approach uses default mappings only (i.e. no custom mappings defined in an
     * ErrorTranslator) and a passed in fallback ErrorDef.
     */
    public static <T> CallResult<T> errorsTranslated(ErrorDef fallbackServiceErrorDef, ServiceResult serviceResult) {
        return CallResult.errorListWithServiceResult(
                ServiceErrorUtil.getServiceErrors(serviceResult, fallbackServiceErrorDef), serviceResult);
    }

    /**
     * Create CallResult with a list of GCError objects produced using a ServiceErrorUtil getServiceErrors call, and
     * associated serviceResult. This approach uses default mappings only (i.e. no custom mappings defined in an
     * ErrorTranslator) and a passed in fallback ErrorDef.
     */
    public static <T> CallResult<T> errorsTranslated(ErrorDef fallbackServiceErrorDef, ServiceResult serviceResult, Object... fieldNameParameters) {
        return CallResult.errorListWithServiceResult(
                ServiceErrorUtil.getServiceErrors(serviceResult, fallbackServiceErrorDef, fieldNameParameters), serviceResult);
    }


    /**
     *
     * Return true if error list contains error with specific code and category.
     *
     * <p>Note: ErrorCode, Category are not unique - so this is only applicable to errors where code is specific for business case</p>
     *
     * @param errorDef
     * @return
     */
    public boolean hasErrorWithCodeAndCategory(ErrorDef errorDef) {

        if (isOk() || errorDef == null) {
            return false;
        }
        return errors.stream()
                .anyMatch(e -> (e.getCode().equals(errorDef.getCode())
                        && e.getCategory().equals(errorDef.getCategory())));
    }

    /**
     * Two Helper methods to translate a ServiceResult to a CallResult: <br/>
     * - If the ServiceResult {@code isOk()}, then the converter is {@code apply()}ied to translate the payload of the
     * ServiceResult into the desired payload of the CallResult. <br/>
     * - If the ServiceResult {@code isError()}, then the {@code errorsTranslated()} is called with either an
     * ErrorTranslator OR an ErrorDef
     * <p>
     * Example Usage:
     *
     * <pre>
     *     1     return handleServiceResult(
     *     2         easyBakeClient.loadBlueCookie(token.getSwid(), token),
     *     3         cookie -> cookie.getValue(),
     *     4         ErrorDef.EASYBAKE_SERVICE_ERROR
     *     5     );
     * </pre>
     *
     * First, [2] client is called and returns a {@code ServiceResult<Cookie>} <br/>
     * Then [1] handleServiceResult calls .isOk() on the serviceResult. <br/>
     * If the ServiceResult is OK, then [3] a {@code CallResult.ok} is created with the value from the converter
     * applied. <br/>
     * If the ServiceResult is not OK, then [4] the (errorDef, or errorTranslator) will be used to add errors to the
     * CallResult.
     *
     * @param serviceResult
     *            - what came back from a call to an external service
     * @param converter
     *            - converts from type {@code <B>} -> type {@code <A>} with any data tweaks if necessary.
     * @param errorTranslator
     *            - (or <b>errorDef</b>) to aid in the error translation process.
     * @param <A>
     *            - The type of the payload of the CallResult.
     * @param <B>
     *            - The type of the payload of the ServiceResult.
     * @return
     */
    public static <A,B> CallResult<A> handleServiceResult(ServiceResult<B> serviceResult, Function<B, A> converter, ErrorTranslator errorTranslator) {
        CallResult<A> returnResult;
        if (serviceResult.isOk()) {
            returnResult = ok(converter.apply(serviceResult.get()));
        } else {
            returnResult = errorsTranslated(errorTranslator, serviceResult);
        }
        return returnResult;
    }

    public static <A,B> CallResult<A> handleServiceResult(ServiceResult<B> serviceResult, Function<B, A> converter, ErrorDef errorDef) {
        CallResult<A> returnResult;
        if (serviceResult.isOk()) {
            returnResult = ok(converter.apply(serviceResult.get()));
        } else {
            returnResult = errorsTranslated(errorDef, serviceResult);
        }
        return returnResult;
    }

    /**
     * @param serviceResult
     * @param errorTranslator
     * @return
     */
    public static CallResult<Boolean> handleServiceResult(ServiceResult<?> serviceResult, ErrorTranslator errorTranslator) {
        CallResult result;
        if (serviceResult.isOk()) {
            result = CallResult.ok(true);
        } else {
            result = CallResult.errorsTranslated(errorTranslator, serviceResult);
        }
        return result;
    }

    /**
     * @param serviceResult
     * @param errorTranslator
     * @return
     */
    public static CallResult<?> handleServiceResultWithNoData(ServiceResult<?> serviceResult, ErrorTranslator errorTranslator) {
        CallResult result;
        if (serviceResult.isOk()) {
            result = CallResult.voidOkWithStatusCode(serviceResult.getStatusCode());
        } else {
            result = CallResult.errorsTranslated(errorTranslator, serviceResult);
        }
        return result;
    }

    /**
     *
     * @param serviceResult
     * @param errorTranslator
     * @return
     */
    public static <A> CallResult<A> handleServiceResultGetData(ServiceResult<A> serviceResult, ErrorTranslator errorTranslator) {
        CallResult<A> result;

        if (serviceResult.isOk()) {
            result = CallResult.ok(serviceResult.get());
        } else {
            result = CallResult.errorsTranslated(errorTranslator, serviceResult);
        }

        return result;
    }

    public boolean isError() {
        return CollectionUtils.isNotEmpty(errors);
    }

    public boolean isOk() {
        return !isError();
    }

    public T get() {
        return payload;
    }

    public int getStatusCode() {
        return statusCode;
    }

    private void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ServiceResult getServiceResult() {
        return serviceResult;
    }

    /**
     * Null-safe access to a ServiceResult's ResponseBody. Returns blank String if ServiceResult is null.
     */
    public String getServiceResultResponseBody() {
        return (serviceResult == null) ? "" : serviceResult.getResponseBody();
    }

    /**
     * Null-safe access to a ServiceResult's StatusCode. Returns -1 if ServiceResult is null.
     */
    public int getServiceResultStatusCode() {
        return (serviceResult == null) ? -1 : serviceResult.getStatusCode();
    }

    public void setServiceResult(ServiceResult<T> serviceResult) {
        this.serviceResult = serviceResult;
        if (serviceResult != null) {
            setStatusCode(serviceResult.getStatusCode());
        }
    }

    public List<GCError> getErrors() {
        return errors;
    }

    /**
     * Return errors as advisory.
     * Note - not all errors can be downgraded to advisory
     * @return
     */
    public List<GCError> getErrorsAsAdvisory() {
        return errors.stream()
                .map(e->e.makeAdvisory())
                .collect(Collectors.toList());
    }


    public void setErrors(List<GCError> errors) {
        if (isOk()) {
            throw new IllegalStateException("Converting a CallResult from isOk state to isError state is not allowed.");
        }
        this.errors = errors;
    }

    public void addError(GCError error) {
        if (isOk()) {
            throw new IllegalStateException("Converting a CallResult from isOk state to isError state is not allowed.");
        }
        errors.add(error);
    }

    public boolean containsErrorCode(ErrorCode errorCode) {
        return errors.stream().anyMatch(e -> e.getCode() == errorCode);
    }

    public boolean containsAnyErrorCodeInList(List<ErrorCode> errorCodes) {
        return (errorCodes != null) && errors.stream().anyMatch(e -> errorCodes.contains(e.getCode()));
    }
}
