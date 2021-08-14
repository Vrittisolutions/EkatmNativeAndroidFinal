precision mediump float;
uniform highp vec2 SunShininessStrength;
uniform highp vec3 SunDirection;
uniform highp vec3 SunColor;
uniform lowp samplerCube SkyCubeTexture;
varying highp vec4 xlv_TEXCOORD5;
void main ()
{
  highp vec4 skyColor_1;
  lowp vec4 tmpvar_2;
  tmpvar_2 = textureCube (SkyCubeTexture, xlv_TEXCOORD5.xyz);
  skyColor_1 = tmpvar_2;
  highp vec4 tmpvar_3;
  tmpvar_3.w = 0.0;
  tmpvar_3.xyz = -(SunDirection);
  skyColor_1.xyz = (skyColor_1.xyz + ((SunShininessStrength.y * 
    pow (clamp (dot (normalize(xlv_TEXCOORD5), tmpvar_3), 0.0, 1.0), SunShininessStrength.x)
  ) * SunColor));
  highp float tmpvar_4;
  tmpvar_4 = clamp (((SunDirection.y - 1.0) / -2.0), 0.0, 1.0);
  skyColor_1.xyz = (skyColor_1.xyz * (tmpvar_4 * (tmpvar_4 * 
    (3.0 - (2.0 * tmpvar_4))
  )));
  gl_FragData[0] = skyColor_1;
}

