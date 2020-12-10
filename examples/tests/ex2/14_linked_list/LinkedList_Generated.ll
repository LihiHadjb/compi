@.Element_vtable = global [6 x i8*] [
	i8* bitcast (i1 (i8*, i32, i32, i1)* @Element.Init to i8*),
	i8* bitcast (i32 (i8*)* @Element.GetAge to i8*),
	i8* bitcast (i32 (i8*)* @Element.GetSalary to i8*),
	i8* bitcast (i1 (i8*)* @Element.GetMarried to i8*),
	i8* bitcast (i1 (i8*, i8*)* @Element.Equal to i8*),
	i8* bitcast (i1 (i8*, i32, i32)* @Element.Compare to i8*)
]

@.List_vtable = global [10 x i8*] [
	i8* bitcast (i1 (i8*)* @List.Init to i8*),
	i8* bitcast (i1 (i8*, i8*, i8*, i1)* @List.InitNew to i8*),
	i8* bitcast (i8* (i8*, i8*)* @List.Insert to i8*),
	i8* bitcast (i1 (i8*, i8*)* @List.SetNext to i8*),
	i8* bitcast (i8* (i8*, i8*)* @List.Delete to i8*),
	i8* bitcast (i32 (i8*, i8*)* @List.Search to i8*),
	i8* bitcast (i1 (i8*)* @List.GetEnd to i8*),
	i8* bitcast (i8* (i8*)* @List.GetElem to i8*),
	i8* bitcast (i8* (i8*)* @List.GetNext to i8*),
	i8* bitcast (i1 (i8*)* @List.Print to i8*)
]

@.LL_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @LL.Start to i8*)]

