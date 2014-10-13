import java.util.prefs.BackingStoreException;


public class Term implements Expression {
	
	private String queryTerm;
	boolean notFlag=false;
	boolean barFlag=false;
	StringBuffer sbBuffer=new StringBuffer();
	
	public Term(String string) {
		queryTerm=string;
		// TODO Auto-generated constructor stub
	}

	public String getQueryTerm() {
		return queryTerm;
	}

	public void setQueryTerm(String queryTerm) {
		this.queryTerm = queryTerm;
	}

	@Override
	public String toString()
	{
		if(queryTerm.contains(":") || queryTerm.equals("AND") || queryTerm.equals("OR") || queryTerm.equals("NOT"))
		{
		return queryTerm;
		}
		else if(queryTerm.equals("NOT"))
		{
			queryTerm="AND";
			notFlag=true;
			return queryTerm;
		}
		else
		{
			if(queryTerm.contains("(") || queryTerm.contains("["))
			{
				queryTerm=queryTerm.replace("(","[");
				barFlag=true;
			}
			if(queryTerm.contains(")"))
			{
				queryTerm=queryTerm.replace(")","]");
				barFlag=true;
			}
			if(notFlag)
			{
				queryTerm="<Term:"+queryTerm+">";
				notFlag=false;
			}
			if(!barFlag)
			{
				queryTerm="Term:"+queryTerm;
			}
			if(barFlag)
			{
				sbBuffer.append(queryTerm);
				queryTerm=sbBuffer.insert(sbBuffer.indexOf("[")+1,"Term:").toString();
				
			}
		}
		return queryTerm;
		
	}
	
}
