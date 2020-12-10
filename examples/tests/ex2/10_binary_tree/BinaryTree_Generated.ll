@.BT_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @BT.Start to i8*)]

@.Tree_vtable = global [20 x i8*] [
	i8* bitcast (i1 (i8*, i32)* @Tree.Init to i8*),
	i8* bitcast (i1 (i8*, i8*)* @Tree.SetRight to i8*),
	i8* bitcast (i1 (i8*, i8*)* @Tree.SetLeft to i8*),
	i8* bitcast (i8* (i8*)* @Tree.GetRight to i8*),
	i8* bitcast (i8* (i8*)* @Tree.GetLeft to i8*),
	i8* bitcast (i32 (i8*)* @Tree.GetKey to i8*),
	i8* bitcast (i1 (i8*, i32)* @Tree.SetKey to i8*),
	i8* bitcast (i1 (i8*)* @Tree.GetHas_Right to i8*),
	i8* bitcast (i1 (i8*)* @Tree.GetHas_Left to i8*),
	i8* bitcast (i1 (i8*, i1)* @Tree.SetHas_Left to i8*),
	i8* bitcast (i1 (i8*, i1)* @Tree.SetHas_Right to i8*),
	i8* bitcast (i1 (i8*, i32, i32)* @Tree.Compare to i8*),
	i8* bitcast (i1 (i8*, i32)* @Tree.Insert to i8*),
	i8* bitcast (i1 (i8*, i32)* @Tree.Delete to i8*),
	i8* bitcast (i1 (i8*, i8*, i8*)* @Tree.Remove to i8*),
	i8* bitcast (i1 (i8*, i8*, i8*)* @Tree.RemoveRight to i8*),
	i8* bitcast (i1 (i8*, i8*, i8*)* @Tree.RemoveLeft to i8*),
	i8* bitcast (i32 (i8*, i32)* @Tree.Search to i8*),
	i8* bitcast (i1 (i8*)* @Tree.Print to i8*),
	i8* bitcast (i1 (i8*, i8*)* @Tree.RecPrint to i8*)
]

