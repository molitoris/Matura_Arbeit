//
//  CLASS
//    NameValue - create a handy name-value pair
//
//  DESCRIPTION
//    It is frequently handy to have one or more name-value pairs
//    with which to store named colors, named positions, named textures,
//    and so forth.  Several of the examples use this class.
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//
class NameValue
{
	public String name;
	public Object value;

	public NameValue( String n, Object v )
	{
		name = n;
		value = v;
	}
}
