import java.util.Stack;


public class QueryMain {

	public static void main(String args[])
	{
		final String OPAND="AND";
		final String OPOR="OR";
		final String OPNOT="NOT";
		/*final String TERM="Term:";
		final String TERMNOT="<TERM:";
		final String STARTBRACE="{";
		final String ENDBRACE="}";
		final String ENDANGULAR=">";
		final String CATEGORY="CATEGORY:";
		final String AUTHOR="AUTHOR:"
		final String PLACE="PLACE:";*/
		String defaultOperator="";
		if(defaultOperator.equals("") || defaultOperator==null || defaultOperator.equals(" "))
			defaultOperator="OR";
		String userQuery="Category:War AND Author:Dutt AND Place:Baghdad AND prisoners detainees rebels";
		System.out.println("The Entered Query Is-->"+userQuery);
		String proccessedQuery="";
		PreProcessingQuery pQuery=new PreProcessingQuery();
		try {
			proccessedQuery=pQuery.preProcessingQuery(userQuery,defaultOperator);
			System.out.println("The PreProcessed Query Is-->"+proccessedQuery);
			String[] queryTokens=proccessedQuery.split(" ");
			StringBuffer sbBuffer=new StringBuffer();
			Stack<Expression> operandStack = new Stack<Expression>();
			Stack<Expression> operatorStack= new Stack<Expression>();
			for(int i=0;i<queryTokens.length;i++)
			{
				if(queryTokens[i].equals(OPAND))
				{
					operatorStack.push(new AND());
				}
				if(queryTokens[i].equals(OPOR))
				{
					operatorStack.push(new OR());
				}
				if(queryTokens[i].equals(OPNOT))
				{
					operatorStack.push(new NOT());
				}
				if(!queryTokens[i].equals(OPAND) && !queryTokens[i].equals(OPNOT) && !queryTokens[i].equals(OPOR))
				{
					if(i!=0)
					{
						if(queryTokens[i-1].equals(OPNOT))
						{
							Term term=new Term(queryTokens[i]);
							term.notFlag=true;
							operandStack.push(term);
						}
						else
						{
							if(queryTokens[i].contains("\""))
							{
								String tokString="";
								for(;i<queryTokens.length;i++)
								{
									tokString=queryTokens[i];
									sbBuffer.append(tokString).append(" ").toString();
									if(tokString.endsWith("\""))
										break;
								}
								queryTokens[i]=sbBuffer.toString();
							}
							operandStack.push(new Term(queryTokens[i]));
						}
					}
						else
						{
							if(queryTokens[i].contains("\""))
							{
								String tokString="";
								for(;i<queryTokens.length;i++)
								{
									tokString=queryTokens[i];
									sbBuffer.append(tokString).append(" ").toString();
									if(tokString.endsWith("\""))
										break;
								}
								queryTokens[i]=sbBuffer.toString().trim();
							}
							operandStack.push(new Term(queryTokens[i]));
						}
					}
					if(queryTokens[i].contains("("))
						operatorStack.push(new Bracket("("));

					if(queryTokens[i].contains(")"))
					{
						Expression expression2;
						do
						{
							expression2=operatorStack.pop();

							if(expression2 instanceof AND)
							{
								operandStack.push(new AND(operandStack.pop(),operandStack.pop()));
							}
							if( expression2 instanceof OR)
							{
								operandStack.push(new OR(operandStack.pop(),operandStack.pop()));
							}
							if(expression2 instanceof NOT)
							{
								operandStack.push(new NOT(operandStack.pop(),operandStack.pop()));
							}
						}while(!(expression2 instanceof Bracket));
					}

				}
				while(!operatorStack.isEmpty())
				{
					Expression expression3=operatorStack.pop();
					if(expression3 instanceof AND)
					{
						operandStack.push(new AND(operandStack.pop(),operandStack.pop()));
					}
					if(expression3 instanceof OR)
					{
						operandStack.push(new OR(operandStack.pop(),operandStack.pop()));
					}
					if(expression3 instanceof NOT)
					{
						operandStack.push(new NOT(operandStack.pop(),operandStack.pop()));
					}
				}
				Expression expression=operandStack.pop();
				String FinalParsedQuery=expression.toString();
				System.out.println("The Final Processed query is-->"+"{"+FinalParsedQuery+"}");


			} catch (QueryPreProcessingException e) {
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}

	}
