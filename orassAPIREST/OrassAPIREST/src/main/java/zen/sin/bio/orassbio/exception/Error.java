
package zen.sin.bio.orassbio.exception;

/**
 *
 * @author Momo Ludovic
 */
public enum Error {
  
    DATAEMPTY("DATAEMPTY"),
   FATAL_ERROR("FATAL_ERROR"),
   DUPLICATE_ERROR_INSERT("DUPLICATE_ERROR_INSERT"),
   DUPLICATE_ERROR_UPDATE("DUPLICATE_ERROR_UPDATE"),
   INSERT_ERROR("INSERT_ERROR"),
   UPDATE_ERROR("UPDATE_ERROR"),
   DELETE_ERROR("DELETE_ERROR"),
   NORESULTS_FOUND("NORESULTS_FOUND"),
   ENTITY_RECORDS_EXIST("ENTITY_RECORDS_EXIST"),
   OPERATION_FAILED("OPERATION_FAILED"),
   
   
   
   
   
   //pour les montants 
   CONTROL_FORMAT("ERREUR_FORMAT"),
  
   //pour les dates
   CONTROL_DATE("CONTROL_DATE")
  ;
   
   //pour etude
   
   

  private final String code;

  private Error( String code) {
   
    this.code = code;
  }

    public String getCode() {
        return code;
    }

  
  @Override
  public String toString() {
    return code ;
  }
}