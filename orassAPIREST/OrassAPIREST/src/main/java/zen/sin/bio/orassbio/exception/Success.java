/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zen.sin.bio.orassbio.exception;

/**
 *
 * @author jeanire
 */
public enum Success {

    INSERT_SUCCESS("INSERT_SUCCESS"),
    UPDATE_SUCCESS("UPDATE_SUCCESS"),
    DELETE_SUCCESS("DELETE_SUCCESS"),
    RESULTS_FOUND("RESULTS_FOUND"),
    OPERATION_SUCCESS("OPERATION_SUCCESS");
    private final String code;

    private Success(String code) {

        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
