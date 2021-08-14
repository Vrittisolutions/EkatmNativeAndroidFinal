precision mediump float;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD5;
void main ()
{
  highp vec2 x_1;
  x_1 = (vec2(0.5, 0.5) - abs((xlv_TEXCOORD0.xy - vec2(0.5, 0.5))));
  bvec2 tmpvar_2;
  tmpvar_2 = lessThan (x_1, vec2(0.0, 0.0));
  if (any(tmpvar_2)) {
    discard;
  };
  highp vec4 tmpvar_3;
  tmpvar_3.yzw = vec3(0.0, 0.0, 0.0);
  tmpvar_3.x = (xlv_TEXCOORD5.z / xlv_TEXCOORD5.w);
  gl_FragData[0] = tmpvar_3;
}
